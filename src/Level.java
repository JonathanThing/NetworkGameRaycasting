public class Level {
    
    private int[][] map;
    private int rows;
    private int columns;
    
    public Level(int [][] map) {
        this.map = map;
        this.rows = map.length;
        this.columns = map[0].length;
    }
    
    public int[][] getMap() {
        return map;
    }
    
    public void setMap(int[][] map) {
        this.map = map;
        this.rows = map.length;
        this.columns = map[0].length;
    }
    
    public int getMapTile(int row, int column) {
        return map[row][column];
    }
    
    public void setMapTile(int row, int column, int value) {
        this.map[row][column] = value;
    }
    
    public int getRows() {
        return rows;
    }
    
    public void setRows(int rows) {
        this.rows = rows;
    }
    
    public int getColumns() {
        return columns;
    }
    
    public void setColumns(int columns) {
        this.columns = columns;
    }
    
    
}