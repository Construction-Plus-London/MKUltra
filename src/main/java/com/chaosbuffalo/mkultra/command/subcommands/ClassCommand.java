package com.chaosbuffalo.mkultra.command.subcommands;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.*;
import com.chaosbuffalo.mkultra.network.ModGuiHandler;
import com.chaosbuffalo.mkultra.network.packets.ForceOpenClientGUIPacket;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.server.command.CommandTreeBase;
import net.minecraftforge.server.command.CommandTreeHelp;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;

public class ClassCommand extends CommandTreeBase {

    public ClassCommand() {
        addSubcommand(new Clear());
        addSubcommand(new SwitchCommand());
        addSubcommand(new Learn());
        addSubcommand(new Unlearn());
        addSubcommand(new UnlearnAll());
        addSubcommand(new List());
        addSubcommand(new Active());
        addSubcommand(new Reset());
        addSubcommand(new Talents());
        addSubcommand(new CommandTreeHelp(this)); // MUST be last
    }

    /**
     * Gets the name of the command
     */
    @Nonnull
    @Override
    public String getName() {
        return "class";
    }

    /**
     * Return the required permission level for this command.
     */
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    /**
     * Gets the usage string for the command.
     */
    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender) {
        return "commands.mk.class";
    }

    static abstract class MKClassCommand extends CommandTreeBase {

        @Override
        public int getRequiredPermissionLevel() {
            return 2;
        }

        @Override
        public boolean isUsernameIndex(String[] args, int index) {
            return args.length > 0 && index == 0;
        }

        @Nonnull
        @Override
        public java.util.List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
            if (args.length == 1) {
                return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
            }
            return Collections.emptyList();
        }

        @Nonnull
        @Override
        public String getUsage(ICommandSender sender) {
            return String.format("commands.mk.class.%s", getName());
        }
    }

    static class Clear extends MKClassCommand {

        @Nonnull
        @Override
        public String getName() {
            return "clear";
        }

        @Override
        public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, String[] args) throws CommandException {
            EntityPlayerMP player = null;
            if (args.length == 0) {
                player = getCommandSenderAsPlayer(sender);
            }
            if (args.length >= 1) {
                player = getPlayer(server, sender, args[0]);
            }

            IPlayerData data = MKUPlayerData.get(player);
            if (data == null)
                return;

            data.activateClass(MKURegistry.INVALID_CLASS);
            player.sendMessage(new TextComponentString("Class deactivated"));
        }
    }

    static class SwitchCommand extends MKClassCommand {

        @Nonnull
        @Override
        public String getName() {
            return "switch";
        }

        @Override
        public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, String[] args) throws CommandException {
            EntityPlayerMP player = null;
            ResourceLocation newClassId = null;

            if (args.length == 0) {
                player = getCommandSenderAsPlayer(sender);
            }
            if (args.length >= 1) {
                player = getPlayer(server, sender, args[0]);
            }
            if (args.length >= 2) {
                newClassId = new ResourceLocation(args[1]);
            }

            IPlayerData data = MKUPlayerData.get(player);
            if (data == null)
                return;

            if (newClassId == null) {
                player.sendMessage(new TextComponentString("Opening class switch GUI"));
                MKUltra.packetHandler.sendTo(new ForceOpenClientGUIPacket(ModGuiHandler.CHANGE_CLASS_SCREEN_ADMIN), player);
            } else {
                PlayerClass playerClass = MKURegistry.getClass(newClassId);
                if (playerClass == null)
                    return;

                if (!data.knowsClass(newClassId))
                    return;

                data.activateClass(newClassId);
            }
        }

        @Nonnull
        @Override
        public java.util.List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
            if (args.length == 1) {
                return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
            } else if (args.length == 2) {
                return getListOfStringsMatchingLastWord(args, MKURegistry.REGISTRY_CLASSES.getKeys());
            }
            return Collections.emptyList();
        }
    }

    static class Learn extends MKClassCommand {

        @Nonnull
        @Override
        public String getName() {
            return "learn";
        }

        @Override
        public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, String[] args) throws CommandException {
            EntityPlayerMP player = null;
            ResourceLocation newClassId = null;

            if (args.length == 0) {
                player = getCommandSenderAsPlayer(sender);
            }
            if (args.length >= 1) {
                player = getPlayer(server, sender, args[0]);
            }
            if (args.length >= 2) {
                newClassId = new ResourceLocation(args[1]);
            }

            IPlayerData data = MKUPlayerData.get(player);
            if (data == null)
                return;

            if (newClassId == null) {
                sender.sendMessage(new TextComponentString("Opening class switch GUI"));
                MKUltra.packetHandler.sendTo(new ForceOpenClientGUIPacket(ModGuiHandler.LEARN_CLASS_SCREEN_ADMIN), player);
            } else {
                data.learnClass(IClassProvider.TEACH_ALL, newClassId);
            }
        }

        @Nonnull
        @Override
        public java.util.List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
            if (args.length == 1) {
                return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
            } else if (args.length == 2) {
                return getListOfStringsMatchingLastWord(args, MKURegistry.REGISTRY_CLASSES.getKeys());
            }
            return Collections.emptyList();
        }
    }

    static class Unlearn extends MKClassCommand {

        @Nonnull
        @Override
        public String getName() {
            return "unlearn";
        }

        @Override
        public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, String[] args) throws CommandException {
            EntityPlayerMP player = null;

            if (args.length == 0) {
                player = getCommandSenderAsPlayer(sender);
            }
            if (args.length >= 1) {
                player = getPlayer(server, sender, args[0]);
            }

            IPlayerData data = MKUPlayerData.get(player);
            if (data == null)
                return;

            ResourceLocation classId = data.getClassId();
            if (args.length >= 2) {
                classId = new ResourceLocation(args[1]);
            }

            if (data.knowsClass(classId)) {
                ((PlayerData) data).unlearnClass(classId);
                sender.sendMessage(new TextComponentString("Class unlearned"));
            } else {
                sender.sendMessage(new TextComponentString(String.format("Cannot unlearn %s because you do not know it", classId)));
            }
        }

        @Nonnull
        @Override
        public java.util.List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
            if (args.length == 1) {
                return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
            } else if (args.length == 2) {
                return getListOfStringsMatchingLastWord(args, MKURegistry.REGISTRY_CLASSES.getKeys());
            }
            return Collections.emptyList();
        }
    }

    static class UnlearnAll extends MKClassCommand {

        @Nonnull
        @Override
        public String getName() {
            return "unlearnall";
        }

        @Override
        public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, String[] args) throws CommandException {
            EntityPlayerMP player = null;

            if (args.length == 0) {
                player = getCommandSenderAsPlayer(sender);
            }
            if (args.length >= 1) {
                player = getPlayer(server, sender, args[0]);
            }

            IPlayerData data = MKUPlayerData.get(player);
            if (data == null)
                return;

            for (ResourceLocation classId : data.getKnownClasses()) {
                ((PlayerData) data).unlearnClass(classId);
                sender.sendMessage(new TextComponentString(String.format("Class %s unlearned", classId.toString())));
            }
        }
    }

    static class List extends MKClassCommand {

        @Nonnull
        @Override
        public String getName() {
            return "list";
        }

        @Override
        public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, String[] args) throws CommandException {
            EntityPlayerMP player = null;

            if (args.length == 0) {
                player = getCommandSenderAsPlayer(sender);
            }
            if (args.length >= 1) {
                player = getPlayer(server, sender, args[0]);
            }

            IPlayerData data = MKUPlayerData.get(player);
            if (data == null)
                return;

            sender.sendMessage(new TextComponentString("Known Classes:"));
            for (ResourceLocation classId : data.getKnownClasses()) {
                sender.sendMessage(new TextComponentString(classId.toString()));
            }
        }
    }

    static class Active extends MKClassCommand {

        @Nonnull
        @Override
        public String getName() {
            return "active";
        }

        @Override
        public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, String[] args) throws CommandException {
            EntityPlayerMP player = null;

            if (args.length == 0) {
                player = getCommandSenderAsPlayer(sender);
            }
            if (args.length >= 1) {
                player = getPlayer(server, sender, args[0]);
            }

            IPlayerData data = MKUPlayerData.get(player);
            if (data == null)
                return;

            ITextComponent msg = new TextComponentString("Active Class: ");
            if (data.hasChosenClass()) {
                PlayerClass playerClass = MKURegistry.getClass(data.getClassId());
                if (playerClass != null) {
                    msg.appendSibling(new TextComponentTranslation(playerClass.getTranslationKey()));
                } else {
                    msg.appendText("Invalid class");
                }
            } else {
                msg.appendText("No Class");
            }

            sender.sendMessage(msg);
        }
    }

    static class Reset extends MKClassCommand {

        @Nonnull
        @Override
        public String getName() {
            return "reset";
        }

        @Override
        public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, String[] args) throws CommandException {
            EntityPlayerMP player = null;

            if (args.length == 0) {
                player = getCommandSenderAsPlayer(sender);
            }
            if (args.length >= 1) {
                player = getPlayer(server, sender, args[0]);
            }

            boolean resetTalents = false;
            if (args.length >= 2) {
                resetTalents = ClassCommand.parseBoolean(args[1]);
            }

            PlayerData data = (PlayerData) MKUPlayerData.get(player);
            if (data == null)
                return;

            if (data.resetAbilities(resetTalents)) {
                player.sendMessage(new TextComponentString("Abilities were all unlearned and points were refunded"));
            } else {
                sender.sendMessage(new TextComponentString("Failed to reset class abilities"));
            }
        }
    }

    static class Talents extends MKClassCommand {
        @Nonnull
        @Override
        public String getName() {
            return "talents";
        }

        @Override
        public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, String[] args) throws CommandException {
            EntityPlayerMP player = getCommandSenderAsPlayer(sender);

            player.sendMessage(new TextComponentString("Opening talent view GUI"));
            MKUltra.packetHandler.sendTo(new ForceOpenClientGUIPacket(ModGuiHandler.TALENT_SCREEN_VIEW), player);
        }
    }
}

