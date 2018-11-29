package pokemonHeroes;

public class SceneFunctions {

    protected static Unit[] createQueue(Trainer trainer1, Trainer trainer2) { //Creates the queue according to unit initiative
        Unit[] queue = new Unit[14];
        for (int i = 0; i < 7; i++) {
            if (trainer1.getUnits()[i] != null)
                queue[i] = trainer1.getUnits()[i];
            else
                queue[i] = null;
            if (trainer2.getUnits()[i] != null)
                queue[i + 7] = trainer2.getUnits()[i];
            else
                queue[i+7] = null;
        }
        Unit temp;
        for (int i = 0; i < queue.length; i++) {
            for (int j = 1; j < queue.length - i; j++) {
                if (queue[j-1]!=null && queue[j]!=null)
                    if (queue[j - 1].getInitiative() < queue[j].getInitiative()) {
                        temp = queue[j - 1];
                        queue[j - 1] = queue[j];
                        queue[j] = temp;
                    }

            }
        }

        return queue;
    }

    public static Unit[] updateQueue(Unit[] queue){
        Unit temp = queue[0];
        for(int i=1; i<queue.length; i++){ //Moves entire queue by one spot
            queue[i-1] = queue[i];
        }
        queue[queue.length-1] = temp;

        for (int i=0; i<queue.length; i++){ //Makes sure all null units are at the end
            if (queue[i] == null){
                for (int j=i+1; j<queue.length; j++){
                    queue[j-1]=queue[j];
                }
                queue[queue.length-1]=null;
                break;
            }
        }

        return queue;
    }

    public static boolean spotTaken(int X, int Y, Unit[] queue){ //Checks if the spot a unit wants to move to is taken

        for (int i=0; i<queue.length; i++)
            if (queue[i].getTileX()==X && queue[i].getTileY()==Y)
                return true;

        return false;

    }

    public static boolean inTile(int x, int y, Tile tile){ //Checks which tile was clicked
        if (tile.getLeftX()<x && x<tile.getRightX() && tile.getTopY()<y && y<tile.getBottomY())
            return true;
        return false;
    }
}
