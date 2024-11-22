package org.landon.thirdparty;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import org.landon.scene.SceneManager;

public class Discord {

    private static DiscordRichPresence drp;

    public static void enableRpc() {
        DiscordRPC.INSTANCE.Discord_Initialize("1308259960370499644", new DiscordEventHandlers(), true, "");
        drp = new DiscordRichPresence();
        drp.largeImageKey = "startup";
        drp.startTimestamp = System.currentTimeMillis() + 5 * 60;
        drp.state = "leapengine.org";

        DiscordRPC.INSTANCE.Discord_UpdatePresence(drp);
    }

    public static void updateRpc() {
        drp.details = "Editing " + SceneManager.getCurrentScene().getFile().getName();
        DiscordRPC.INSTANCE.Discord_UpdatePresence(drp);
    }

    public static void disableRpc() {
        DiscordRPC.INSTANCE.Discord_Shutdown();
    }

}
