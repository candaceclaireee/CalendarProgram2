package backend;


import javafx.beans.property.SimpleStringProperty;
import javafx.scene.paint.Color;

public class Showing {
    private SimpleStringProperty time;
    private SimpleStringProperty event;
    private Color color;
    private int valueStartHour;
    private int valueEndHour;
    private int valueStartMin;
    private int valueEndMin;

    public Showing(String time, String event) {
        this.time = new SimpleStringProperty(time);
        this.event = new SimpleStringProperty(event);
    }

    public String getTime() {
        return time.get();
    }

    public String getEvent() {
        return event.get();
    }

    public void setTime(String newTime) {
        time.set(newTime);
    }

    public void setEvent(String newEvent) {
        event.set(newEvent);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }


    public int getValueStartHour() {
        return valueStartHour;
    }

    public void setValueStartHour(int valueStartHour) {
        this.valueStartHour = valueStartHour;
    }

    public int getValueEndHour() {
        return valueEndHour;
    }

    public void setValueEndHour(int valueEndHour) {
        this.valueEndHour = valueEndHour;
    }

    public int getValueStartMin() {
        return valueStartMin;
    }

    public void setValueStartMin(int valueStartMin) {
        this.valueStartMin = valueStartMin;
    }

    public int getValueEndMin() {
        return valueEndMin;
    }

    public void setValueEndMin(int valueEndMin) {
        this.valueEndMin = valueEndMin;
    }
}
