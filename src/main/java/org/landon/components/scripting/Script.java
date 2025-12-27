package org.landon.components.scripting;

import org.landon.components.Component;
import org.landon.editor.windows.inspector.fields.LeapFile;
import org.landon.editor.windows.logger.Logger;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

public class Script extends Component {

    public LeapFile script;
    private transient Globals globals;
    private transient LuaValue startFn;
    private transient LuaValue updateFn;
    private transient boolean scriptReady;

    public Script() {
        super("Script", true, true);
    }

    @Override
    public void start() {
        scriptReady = false;
        if (script == null || script.getFile() == null) {
            Logger.error("No script file assigned to Script component");
            return;
        }

        try {
            globals = JsePlatform.standardGlobals();
            LuaValue chunk = globals.loadfile(script.getFile().getAbsolutePath());
            chunk.call();

            startFn = globals.get("start");
            if (!startFn.isfunction()) {
                startFn = null;
            }

            updateFn = globals.get("update");
            if (!updateFn.isfunction()) {
                updateFn = null;
            }

            scriptReady = true;
            invokeLua(startFn, "start");
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    @Override
    public void update() {
        if (!scriptReady) return;
        invokeLua(updateFn, "update");
    }

    private void invokeLua(LuaValue fn, String name) {
        if (fn == null) return;
        try {
            fn.call();
        } catch (Exception e) {
            Logger.error(e);
        }
    }
}
