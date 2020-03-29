package samples;

import samples.camera.Camera;

public abstract class DefaultSample implements Sample{
    protected String title;
    protected int width;
    protected int height;
    protected double scale;
    protected Camera camera;

    public DefaultSample(String title, int width, int height, double scale){
        this.title=title;
        this.width = width;
        this.height=height;
        this.scale = scale;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public double getScale() {
        return scale;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public Camera getActiveCamera(){
        return camera;
    }
}