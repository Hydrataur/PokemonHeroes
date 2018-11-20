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
        for(int i=1; i<queue.length; i++){
            queue[i-1] = queue[i];
        }
        queue[queue.length-1] = temp;
        return queue;
    }
}
