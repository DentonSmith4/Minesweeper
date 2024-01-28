// Denton Smith smi02055
// Charley Hatterman hatte064
public class Cell {
    private boolean revealed;
    private boolean isFlagged;
    private String status;
    /*
    "-" = Blank (defualt value)
    "F" = Flagged by user
    "M" = Mine
    "0...9" = Number of mines in surrounding 3x3 tile.
    */

    public Cell(boolean flagged,boolean revealed, String status) {
        isFlagged = false;//true if user wants to flag cell
        this.revealed = revealed;
        this.status = status;
    }

    public boolean getRevealed() {
        return revealed;
    }

    public boolean isFlagged(){
        return isFlagged;
    }

    public void setFlagged(boolean f){
        isFlagged = f;
    }


    public void setRevealed(boolean r) {
        revealed = r;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String c) {
        status = c;
    }
}