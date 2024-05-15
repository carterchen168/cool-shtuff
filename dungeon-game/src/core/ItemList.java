package core;

public class ItemList {
    public static final Item HEALTH_POTION = new Item("potion", "images/healingpotion.gif", "consumable", 4, 3);
    public static final Item BOW = new Item("bow", "images/bow.png", "weapon", 2, 2);
    public static final Item KEY = new Item("key", "images/key.png", "item", 4, 3);
    public static final Item SWORD = new Item("sword", "images/sword.png", "weapon", 2, 2);
    public static final Item FINAL_EXAM_PAPERS = new Item("exams", "images/exams.png", "item", 3, 3);

    public static Item getItem(String name) {
        if (name == "bow") {
            return BOW;
        }
        if (name == "potion") {
            return HEALTH_POTION;
        }
        if (name == "key") {
            return KEY;
        }
        if (name == "sword") {
            return SWORD;
        }
        if (name == "exams") {
            return FINAL_EXAM_PAPERS;
        }
        return null;
    }
}
