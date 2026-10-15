public class Trailer {
    private String type;
    private int weight;
    private int wheels;

    public Trailer(String type, int weight, int wheels) {
        this.type = type;
        this.weight = weight;
        this.wheels = wheels;
    }

    public String getType() { return type; }
    public int getWeight() { return weight; }
    public int getWheels() { return wheels; }
    @Override
    public String toString() {
        return "Trailer [Type: " + type + ", Weight: " + weight + " lbs, Wheels: " + wheels + "]";
    }
}
