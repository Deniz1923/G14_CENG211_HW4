package game;


import game.boxes.Box;

import java.util.ArrayList;

/**
 * You should choose an appropriate data structure from the Collections framework to use inside
 * the BoxGrid class. At the top of that file, explain your reasoning for this choice in a few
 * sentences inside a comment block. Mark the start of the comment block with “ANSWER TO
 * COLLECTIONS QUESTION:” to make it more visible.
 */
public class BoxGrid {
    // The grid is a List of Rows, where each Row is a List of Boxes
    private final ArrayList<ArrayList<Box>> grid;

    public BoxGrid() {
        this.grid = new ArrayList<>();
        initializeGrid();
    }

    private void initializeGrid() {
    }

    // Helper method to access boxes easily
    public Box getBox(int row, int col) {
        return grid.get(row).get(col);
    }

    public void setBox(int row, int col, Box box) {
        grid.get(row).set(col, box);
    }


}

