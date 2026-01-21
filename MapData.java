import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

public class MapData {
    public static final int TYPE_SPACE = 0;
    public static final int TYPE_WALL = 1;
    //public static final int TYPE_OTHERS = 2;
    public static final int TYPE_ITEM = 2;
    public static final int TYPE_HEAL = 3;   // 回復アイテム
    public static final int TYPE_GOLE = 4;//ゴール

    private static final String ITEM_SHEET_PATH = "png/pipo-etcchara002a.png";
    private static final int SHEET_ROWS = 4;
    private static final int SHEET_COLS = 3;

    private Image[] mapImages;
    private ImageView[][] mapImageViews;
    private int[][] maps;
    private int width; // width of the map
    private int height; // height of the map

    MapData(int x, int y) {
        mapImages = new Image[5];
        mapImageViews = new ImageView[y][x];

        // 床と壁の画像
        mapImages[TYPE_SPACE] = new Image("png/SPACE.png");
        mapImages[TYPE_WALL] = new Image("png/WALL.png");

        // アイテムの画像
        try {
            Image itemSheet = new Image(ITEM_SHEET_PATH);
            // TYPE_ITEM (2): 1行目（キラキラ）
            mapImages[TYPE_ITEM] = extractImage(itemSheet, 0, 0);
            // TYPE_HEAL (3): 3行目（宝石）
            mapImages[TYPE_HEAL] = extractImage(itemSheet, 2, 0);
        } catch (Exception e) {
            System.err.println("画像の切り出しに失敗しました: " + e.getMessage());
        }
        mapImages[TYPE_GOLE] = new Image("png/goalpicture.png");

        width = x;
        height = y;
        maps = new int[y][x];

        fillMap(MapData.TYPE_WALL);
        digMap(1, 3);
        goleSetting(TYPE_GOLE);
        scatterItems(TYPE_ITEM, 2);   
        scatterItems(TYPE_HEAL, 2);   
        setImageViews();
    }

    private Image extractImage(Image sheet, int row, int col) {
        double frameWidthD = sheet.getWidth() / SHEET_COLS;
        double frameHeightD = sheet.getHeight() / SHEET_ROWS;
        
        PixelReader reader = sheet.getPixelReader();
        int startX = (int) Math.round(col * frameWidthD);
        int startY = (int) Math.round(row * frameHeightD);
        
        return new WritableImage(reader, startX, startY, (int)frameWidthD, (int)frameHeightD);
    }
    // fill two-dimentional arrays with a given number (maps[y][x])
    private void fillMap(int type) {
        for (int y = 0; y < height; y ++) {
            for (int x = 0; x < width; x++) {
                maps[y][x] = type;
            }
        }
    }

    // dig walls for making roads
    private void digMap(int x, int y) {
        setMap(x, y, MapData.TYPE_SPACE);
        int[][] dl = { { 0, 1 }, { 0, -1 }, { -1, 0 }, { 1, 0 } };
        int[] tmp;

        for (int i = 0; i < dl.length; i ++) {
            int r = (int) (Math.random() * dl.length);
            tmp = dl[i];
            dl[i] = dl[r];
            dl[r] = tmp;
        }

        for (int i = 0; i < dl.length; i ++) {
            int dx = dl[i][0];
            int dy = dl[i][1];
            if (getMap(x + dx * 2, y + dy * 2) == MapData.TYPE_WALL) {
                setMap(x + dx, y + dy, MapData.TYPE_SPACE);
                digMap(x + dx * 2, y + dy * 2);
            }
        }
    }

    public int getMap(int x, int y) {
        if (x < 0 || width <= x || y < 0 || height <= y) {
            return -1;
        }
        return maps[y][x];
    }

    public void setMap(int x, int y, int type) {
        if (x < 1 || width <= x - 1 || y < 1 || height <= y - 1) {
            return;
        }
        maps[y][x] = type;
    }

    public ImageView getImageView(int x, int y) {
        return mapImageViews[y][x];
    }

    public void setImageViews() {
        for (int y = 0; y < height; y ++) {
            for (int x = 0; x < width; x++) {
                mapImageViews[y][x] = new ImageView(mapImages[maps[y][x]]);
            }
        }
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
    
    private void goleSetting(int type) {
        for (int y = getHeight() - 1; y >= 0; y--) {
            for (int x = getWidth() - 1; x >= 0; x--) {
                if (getMap(x,y) == TYPE_SPACE) {
                   maps[y][x] = type;
                   return;
                }
            }
        }
    }
    private void scatterItems(int type, int count) {
    int placed = 0;
    while (placed < count) {
        int rx = (int) (Math.random() * width);
        int ry = (int) (Math.random() * height);
        // 床(TYPE_SPACE)かつ、スタート地点(1,1)以外に配置
        if (getMap(rx, ry) == TYPE_SPACE && !(rx == 1 && ry == 1)) {
            maps[ry][rx] = type;
            placed++;
        }
    }
}
}

class Fireball {
    public int x, y;
    private int dx; 
    private boolean alive = true;

    public Fireball(int x, int y, int dx) {
        this.x = x;
        this.y = y;
        this.dx = dx;
    }

    public void move(int mapWidth) {
        x += dx;
        if (x < 0 || x >= mapWidth) {
            alive = false;
        }
    }

    public boolean isAlive() {
        return alive;
    }
}
