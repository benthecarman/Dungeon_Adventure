package sa_b_2.coms309.dungeonadventure.game;


import android.graphics.Point;

import java.util.Random;

public class Floor {

    private Room[][] rooms;
    private int numRooms=10;
    private Random rand;
    private EnemyManager enemyManager;
    private Room start;
    private Point currentRoomCoords;
    private int floorNumber;

    /**
     * Constructs a floor to add to the dungeon
     * @param enemyManager to generate enemies in rooms on the floor
     * @param floorNumber index of the floor in the dungeon
     */
    public Floor(EnemyManager enemyManager, int floorNumber) {
        rooms = new Room[15][15];
        this.enemyManager=enemyManager;
        this.floorNumber = floorNumber;
        generateFloor();
        addDoors();
        currentRoomCoords = getStart().getCoords();
    }

    /**
     * Generates a random floor layout
     */
    public void generateFloor(){
        start=new Room();
        rooms[7][7] = start;
        int count=0;
        rand=new Random();
        int randX;
        int randY;
        Point coord;
        while(count<numRooms){
            randX = rand.nextInt(15);
            randY = rand.nextInt(15);
            if (rooms[randY][randX] == null && hasNeighbors(randY, randX)) {
                coord = new Point(randX, randY);
                if (count == numRooms - 1) {
                    rooms[randY][randX] = new Room(enemyManager.populateRooms(), coord, true);
                } else {
                    rooms[randY][randX] = new Room(enemyManager.populateRooms(), coord, false);
                }
                count++;
            }
        }
    }
    /**
     * Adds the doors to the rooms
     */
    public void addDoors(){
        for (int i=0;i<rooms.length;i++){
            for(int j=0;j<rooms[i].length;j++){
                if(rooms[i][j]!=null)
                    findNeighbors(i, j);
            }
        }
    }

    /**
     * Finds neighbors to see if a room is neighboring another and gives room a door to connect the rooms
     * @param r row
     * @param c column
     */
    public void findNeighbors(int r, int c){
        for (int nr = Math.max(0, r - 1); nr <= Math.min(r + 1, rooms.length - 1); ++nr){
            for (int nc = Math.max(0, c - 1); nc <= Math.min(c + 1, rooms[0].length - 1); ++nc) {
                if (rooms[nr][nc]!=null&&rooms[r][c]!=null&&(nr!=r&&nc==c||nr==r&&nc!=c))  {
                    if(nr+1==r&&nc==c){
                        rooms[r][c].addDoor(new Door(Direction.NORTH));
                    }
                    else if(nr-1==r&&nc==c){
                        rooms[r][c].addDoor(new Door(Direction.SOUTH));
                    }
                    else if(nr==r&&nc+1==c){
                        rooms[r][c].addDoor(new Door(Direction.WEST));
                    }
                    else if(nr==r&&nc-1==c){
                        rooms[r][c].addDoor(new Door(Direction.EAST));
                    }
                }
            }
        }
    }
    /**
     * Finds neighbors to see if a room can be placed
     * @param r row
     * @param c column
     */
    public boolean hasNeighbors(int r, int c){
        for (int nr = Math.max(0, r - 1); nr <= Math.min(r + 1, rooms.length - 1); ++nr){
            for (int nc = Math.max(0, c - 1); nc <= Math.min(c + 1, rooms[0].length - 1); ++nc) {
                if (rooms[nr][nc]!=null)  {
                    if ((nr + 1 == r && nc == c) || (nr - 1 == r && nc == c) || (nr == r && nc + 1 == c) || (nr == r && nc - 1 == c)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Returns the starting room
     * @return start room
     */
    public Room getStart(){
        return start;
    }

    /**
     * Returns the current room
     * @return room currently in
     */
    public Room getCurrRoom() {
        return rooms[currentRoomCoords.y][currentRoomCoords.x];
    }

    /**
     * Returns the floor number
     * @return floor number
     */
    public int getFloorNumber() {
        return floorNumber;
    }

    /**
     * Sets the current room
     * @param roomCoords to set current room
     */
    public void setRoom(Point roomCoords) {
        currentRoomCoords = roomCoords;
    }
}
