package net.grewind.palimer.bot.commands;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class Command {
    /**
     * The Soil of a {@link Command}.
     * <p>
     * Acts as the text, that has to initiate every {@link Command}.
     */
    public static final String SOIL = "!";
    private final String tree;
    private final String root;
    private final String crown;
    private final String[] branches;

    public Command(@NotNull String raw) {
        this.tree = raw.matches(SOIL + ".*") ? raw.substring(SOIL.length()) : raw;
        this.branches = Arrays.stream(this.tree.split("\\s"))
                .filter(s -> !s.isBlank())
                .toArray(String[]::new);
        this.root = this.branches[0];
        this.crown = this.tree.replaceFirst(this.root + "\\s*", "");
    }

    /**
     * The Tree of a {@link Command}.
     * <p>
     * Represents the entire {@link Command} without its {@link #SOIL}.
     *
     * @return tree
     */
    public String getTree() {
        return tree;
    }

    /**
     * The Branches of a {@link Command}.
     * <p>
     * Represents the segments of the {@link #getTree() Tree} separated by whitespaces.
     *
     * @return branches
     */
    public String[] getBranches() {
        return branches;
    }

    /**
     * The Root of a {@link Command}.
     * <p>
     * Represents the first {@link #getBranches() Branch} of the {@link #getTree() Tree}.
     *
     * @return root
     */
    public String getRoot() {
        return root;
    }

    /**
     * The Crown of a {@link Command}.
     * <p>
     * Represents the {@link #getTree() Tree} without the {@link #getRoot() Root}.
     *
     * @return crown
     */
    public String getCrown() {
        return crown;
    }

    /**
     * The {@link #getBranches() Branches} of the {@link #getCrown() Crown} of a {@link Command}.
     * <p>
     * Represents all {@link #getBranches() Branches} of the {@link #getTree() Tree} <b>but</b> the {@link #getRoot() Root}.
     *
     * @return crownBranches
     */
    public String[] getCrownBranches() {
        if (branches.length == 0) {
            return null;
        }
        String[] crownBranches = new String[branches.length - 1];
        if (crownBranches.length == 0) {
            return crownBranches;
        }
        System.arraycopy(branches, 1, crownBranches, 0, crownBranches.length);
        return crownBranches;
    }
}
