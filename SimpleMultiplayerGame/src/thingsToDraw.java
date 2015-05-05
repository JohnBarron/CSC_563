
public class thingsToDraw {
    private int[] shipXofPlayer;
    private int[] shipYofPlayer;
    private int[] coinX;
    private int[] coinY;
    
    //thingsToDraw(){
    //    shipXofPlayer = new int[3];
    //    shipYofPlayer = new int[3];
    //}
    
    thingsToDraw(int numPlayers){
        shipXofPlayer = new int[numPlayers];
        shipYofPlayer = new int[numPlayers];
        for(int i = 0; i < numPlayers; i++){
            shipXofPlayer[0] = 0;
            shipYofPlayer[0] = 0;
        }
    }
    
    thingsToDraw(int numPlayers, int numCoins){
        shipXofPlayer = new int[numPlayers];
        shipYofPlayer = new int[numPlayers];
        coinX = new int[numCoins];
        coinY = new int[numCoins];
        for(int i = 0; i < numPlayers; i++){
            shipXofPlayer[0] = 0;
            shipYofPlayer[0] = 0;
        }
        for(int i = 0; i < numCoins; i++){
            coinX[0] = 0;
            coinY[0] = 0;
        }
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