public class Level {
    
    private Environment[][] map;
    private int rows;
    private int columns;
    
    public Level(Environment [][] map) {
        this.map = map;
        this.rows = map.length;
        this.columns = map[0].length;
    }
    
    public Environment[][] getMap() {
        return map;
    }
    
    public void setMap(Environment[][] map) {
        this.map = map;
        this.rows = map.length;
        this.columns = map[0].length;
    }
    
    public Environment getMapTile(int row, int column) {
        return map[row][column];
    }

    public Environment getMapTile(Vector position) {
        return map[(int) position.getX()][(int) position.getY()];
    }
    
    public void setMapTile(int row, int column, Environment value) {
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