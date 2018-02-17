package sa_b_2.coms309.dungeonadventure.game;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import sa_b_2.coms309.dungeonadventure.R;

import static sa_b_2.coms309.dungeonadventure.game.Constants.context;

/**
 * Manages list of items
 * Created by Tanner on 9/17/2017.
 */


public class ItemManager {
    //pickupDisplay is true when there is a currentItem and count < 30
    boolean pickupDisplay;
    int count;
    //List of items to be generated in single room
    private List<Item> items;
    //List of all possible Items
    private List<Item> itemList;
    private Item currentItem;

    /**
     * Makes a itemManager, used to group items and draw them
     *
     * @param is used to make items from a text document
     */
    public ItemManager(InputStreamReader is) {
        items = new LinkedList<>();
        if (Constants.itemList == null)
            populateItems(is);
        itemList = new LinkedList<>(Constants.itemList);
        populateRoomItems();

        pickupDisplay = false;
        currentItem = null;
        count = 0;
    }

    /**
     * Selects random item from room items
     * @param items list of room items
     * @return random item
     */
    public static Item randomItem(List<Item> items) {
        int size = items.size();
        int index = randInt(0, size - 1);
        return items.get(index);
    }

    /**
     * Chooses random integer between a min and max, used for selecting which items will spawn
     * @param min minimum integer
     * @param max maximum integer
     * @return random integer
     */
    public static int randInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    /**
     * returns all possible items
     * @return all possible items
     */
    public List<Item> getItemList() {
        return itemList;
    }

    /**
     * sets itemList
     * @param itemList
     */
    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    /**
     * returns room items
     * @return room items
     */
    public List<Item> getItems() {
        return items;
    }

    /**
     * sets items
     * @param items
     */
    public void setItems(List<Item> items) {
        this.items = items;
    }

    /**
     * Populates itemList for all possible items from text document
     * @param is used to connect to document
     */
    private void populateItems(InputStreamReader is) {

        Constants.itemList = new LinkedList<>();
        Scanner scan = new Scanner(is);

        while (scan.hasNext()) {
            String line = scan.nextLine();
            if (line.equals("ITEM")) {
                line = scan.nextLine();
                String name = null;
                String desc = null;
                int value = 0;

                float shotSpeed = 0;
                float shotDamage = 0;
                short shotSize = 0;
                short range = 0;
                double moveSpeed = 0;

                Bitmap image = null;

                while (!line.equals("END") && scan.hasNext()) {
                    if (line.equals("Name:")) {
                        line = scan.nextLine();
                        name = line;
                    }
                    if (line.equals("Value:")) {
                        line = scan.nextLine();
                        value = Integer.parseInt(line);
                    }
                    if (line.equals("shotSpeed:")) {
                        line = scan.nextLine();
                        shotSpeed = Integer.parseInt(line);
                    }
                    if (line.equals("shotDamage:")) {
                        line = scan.nextLine();
                        shotDamage = Integer.parseInt(line);
                    }
                    if (line.equals("shotSize:")) {
                        line = scan.nextLine();
                        shotSize = (short) Integer.parseInt(line);
                    }
                    if (line.equals("range:")) {
                        line = scan.nextLine();
                        range = (short) Integer.parseInt(line);
                    }
                    if (line.equals("moveSpeed:")) {
                        line = scan.nextLine();
                        moveSpeed = Integer.parseInt(line);
                    }
                    if (line.equals("Desc:")) {
                        line = scan.nextLine();
                        desc = line;
                    }
                    if (line.equals("Image:")) {
                        line = scan.nextLine();
                        int resId = context.getResources().getIdentifier(line, "drawable", context.getPackageName());
                        image = BitmapFactory.decodeResource(context.getResources(), resId);
                    }
                    if (scan.hasNext()) {
                        line = scan.nextLine();
                    }
                }
                Item i = new Item(0, 0, name, value, image, shotSpeed, shotDamage, shotSize, range, moveSpeed, 0);
                if (desc != null)
                    i.setDesc(desc);
                Constants.itemList.add(i);
            }
        }
        scan.close();
    }

    /**
     * Checks if player collides with any room items. If so, removes that item and adds it to the player inventory
     * @param player player to be checked
     * @return true if player collides, else false
     */
    public boolean playerCollide(Player player) {
        for (Item it : items) {
            if (it.playerCollide(player)) {
                if (!player.heal(it.getHealth()) && it.getHealth() != 0)
                    return false;

                if (!it.getName().equals("heart")) {
//                    if(currentItem!=null){
//                        player.dropItem(currentItem, this);
//                    }
                    if (player.getPlayerItems().size() < player.getInventorySize()) {
                        player.addItem(it);
                        player.processItemList();
                        count = 0;
                        pickupDisplay = true;
                        currentItem = it;
                        if (it.getName().equalsIgnoreCase("Rubber Shots")) {
                            player.setShotImage(BitmapFactory.decodeResource(Constants.context.getResources(), R.drawable.rubber));
                        }
                        items.remove(it);
                    }


                }

                return true;
            }
        }
        return false;
    }

    /**
     * adds item to room items
     * @param item item to be added to item list
     */
    public void addItem(Item item) {
        items.add(item);
    }

    /**
     * Used to ensure no two items intersect
     * @param rect item to be compared to
     * @return true if they overlap, else false
     */
    public boolean Overlap(Rect rect) {
        for (Item it : items) {
            if (RectF.intersects(new RectF(rect), it.getRectangle())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds to the room items list, using randomItem and randomInt. A high value int will make items rarer
     */
    public void populateRoomItems() {
        int itemsPerRoom = 0;
        while (itemsPerRoom < 2) {
            Item temp = randomItem(itemList);
            int random = randInt(0, temp.getValue());
            Iterator<Item> iter = itemList.iterator();
            int x = 60;
            if (random < 10) {
                boolean valid = false;
                int left = 0;
                int top = 0;
                while (!valid) {
                    left = (int) (Math.random() * Constants.GAMEWIDTH);
                    top = (int) (Math.random() * Constants.GAMEHEIGHT);

                    Rect tempR = new Rect(left, top, left + x, top + x);
                    if (!Overlap(tempR) && left + x < Constants.GAMEWIDTH && top + x < Constants.GAMEHEIGHT) {
                        valid = true;
                    }
                }
                temp.setRectangle(left, top, left + x, top + x);
                items.add(temp);
                while (iter.hasNext()) {
                    Item it = iter.next();

                    if (it == temp) {
                        iter.remove();
                    }
                }
                itemsPerRoom++;
            }
        }
        populateHearts();
    }

    /**
     * Populates hearts for healing the player
     */
    public void populateHearts() {
        int random = randInt(0, 3);
        Bitmap image = BitmapFactory.decodeResource(Constants.context.getResources(), R.drawable.heart);
        for (int i = 0; i < random; i++) {
            boolean valid = false;
            int left = 0;
            int top = 0;
            while (!valid) {
                left = (int) (Math.random() * Constants.GAMEWIDTH);
                top = (int) (Math.random() * Constants.GAMEHEIGHT);
                int x = 20;
                Rect temp = new Rect(left, top, left + x, top + x);
                if (!Overlap(temp) && left + x < Constants.GAMEWIDTH && top + x < Constants.GAMEHEIGHT) {
                    valid = true;
                }
            }
            int x = 20;
            Item heart = new Item(left, top, "heart", 0, image, 0, 0, (short) 0, (short) 0, 0, 5);
            heart.setRectangle(left, top, left + x, top + x);
            items.add(heart);
        }
    }

    /**
     * Draws all items in the room list
     * @param canvas canvas to be drawn to
     */
    public void draw(Canvas canvas) {
        for (Item it : items) {
            it.draw(canvas);
        }
        if (pickupDisplay && count < 30) {
            currentItem.drawPickup(canvas);
            count++;
        } else {
            count = 0;
            pickupDisplay = false;
        }
    }
}