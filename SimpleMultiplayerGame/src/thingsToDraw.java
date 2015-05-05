
public class thingsToDraw {
    private int[] shipXofPlayer;
    private int[] shipYofPlayer;
    
    thingsToDraw(){
        shipXofPlayer = new int[3];
        shipYofPlayer = new int[3];
    }
    
    thingsToDraw(int numPlayers){
        shipXofPlayer = new int[numPlayers];
        shipYofPlayer = new int[numPlayers];
    }

    public int[] getShipXofPlayer() {
        return shipXofPlayer;
    }

    public void setShipXofPlayer(int i, int shipXofPlayer) {
        this.shipXofPlayer[i] = shipXofPlayer;
    }

    public int[] getShipYofPlayer() {
        return shipYofPlayer;
    }

    public void setShipYofPlayer(int i, int shipYofPlayer) {
        this.shipYofPlayer[i] = shipYofPlayer;
    }
}