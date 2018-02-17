package sa_b_2.coms309.dungeonadventure.game;


public class Dungeon {

    private EnemyManager enemyManager;
    private Floor[] floors;

    /**
     * Constructs a dungeon for the player to go through
     * @param enemyManager to create enemies in the dungeon
     */
    public Dungeon(EnemyManager enemyManager) {
        this.enemyManager = enemyManager;
        floors = new Floor[5];
        generateDungeon();
    }

    /**
     * generates an array of floors
     */
    public void generateDungeon() {
        for (int i = 0; i < floors.length; i++) {
            floors[i] = new Floor(enemyManager, i);
        }
    }

    /**
     * Returns a floor at index given
     * @param i index of floor
     * @return floor at index given
     */
    public Floor getFloor(int i) {
        return floors[i];
    }

    /**
     * Returns the array of floors
     * @return floors in dungeon
     */
    public Floor[] getFloors() {
        return floors;
    }
}
