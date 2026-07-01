import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class MyPacker {
    public static void main(String[] args) {
        TexturePacker.Settings settings = new TexturePacker.Settings();
        settings.maxWidth = 2048;
        settings.maxHeight = 2048;

        TexturePacker.process(settings,
            "C:\\Users\\Asus\\Desktop\\UNI\\Term2\\Advanced Programming\\P4\\assets\\animations\\raw_knight_frames",
            "C:\\Users\\Asus\\Desktop\\UNI\\Term2\\Advanced Programming\\P4\\assets\\animations\\knight_atlas",
            "knight"
        );

        TexturePacker.process(settings,
            "C:\\Users\\Asus\\Desktop\\UNI\\Term2\\Advanced Programming\\P4\\assets\\animations\\raw_HUD_frames",
            "C:\\Users\\Asus\\Desktop\\UNI\\Term2\\Advanced Programming\\P4\\assets\\animations\\HUD_atlas",
            "hud"
        );

        TexturePacker.process(settings,
            "C:\\Users\\Asus\\Desktop\\UNI\\Term2\\Advanced Programming\\P4\\assets\\animations\\raw_effects_frames",
            "C:\\Users\\Asus\\Desktop\\UNI\\Term2\\Advanced Programming\\P4\\assets\\animations\\effects_atlas",
            "effects"
        );
    }
}
