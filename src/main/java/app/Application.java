package app;

import controls.Label;
import io.github.humbleui.jwm.*;
import io.github.humbleui.jwm.skija.EventFrameSkija;
import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.Surface;
import misc.CoordinateSystem2i;

import java.io.File;
import java.util.function.Consumer;

import static app.Color.APP_BACKGROUND_COLOR;
import static app.Color.PANEL_BACKGROUND_COLOR;

/**
 * Класс окна приложения
 */
public class Application implements Consumer<Event> {
    /**
     * окно приложения
     */
    private final Window window;
    /**
     * отступы панелей
     */
    private static final int PANEL_PADDING = 5;
    /**
     * радиус скругления элементов
     */
    public static final int C_RAD_IN_PX = 4;
    /**
     * Первый заголовок
     */
    private final Label label;
    /**
     * Первый заголовок
     */
    private final Label label2;
    /**
     * Первый заголовок
     */
    private final Label label3;


    /**
     * Конструктор окна приложения
     */
    public Application() {
        Label label1;


        // создаём окно
        window = App.makeWindow();

        // создаём первый заголовок
        label1 = new Label(window, true, PANEL_BACKGROUND_COLOR, PANEL_PADDING,
                 "Привет, мир!");

        // создаём второй заголовок
        label1 = new Label(window, true, PANEL_BACKGROUND_COLOR, PANEL_PADDING,
                 "Второй заголовок");

        // создаём третий заголовок
        label1 = new Label(window, true, PANEL_BACKGROUND_COLOR, PANEL_PADDING,
                "Это тоже заголовок");


        // задаём обработчиком событий текущий объект
        window.setEventListener(this);
        // задаём заголовок
        window.setTitle("Java 2D");
        // задаём размер окна
        window.setWindowSize(900, 900);
        // задаём его положение
        window.setWindowPosition(100, 100);
        // задаём иконку

        switch (Platform.CURRENT) {
            case WINDOWS -> window.setIcon(new File("src/main/resources/windows.ico"));
            case MACOS -> window.setIcon(new File("src/main/resources/macos.icns"));
        }

        // создаём первый заголовок
        label1 = new Label(window, true, PANEL_BACKGROUND_COLOR, PANEL_PADDING,
                "Привет, мир!");

        // создаём второй заголовок
        label = label1;
        label1 = new Label(window, true, PANEL_BACKGROUND_COLOR, PANEL_PADDING,
                 "Второй заголовок");

        // создаём третий заголовок
        label2 = label1;
        label1 = new Label(window, true, PANEL_BACKGROUND_COLOR, PANEL_PADDING,
                 "Это тоже заголовок");

        // названия слоёв, которые будем перебирать
        label3 = label1;
        String[] layerNames = new String[]{
                "LayerGLSkija", "LayerRasterSkija"
        };

        // перебираем слои
        for (String layerName : layerNames) {
            String className = "io.github.humbleui.jwm.skija." + layerName;
            try {
                Layer layer = (Layer) Class.forName(className).getDeclaredConstructor().newInstance();
                window.setLayer(layer);
                break;
            } catch (Exception e) {
                System.out.println("Ошибка создания слоя " + className);
            }
        }

        // если окну не присвоен ни один из слоёв
        if (window._layer == null)
            throw new RuntimeException("Нет доступных слоёв для создания");

        // делаем окно видимым
        window.setVisible(true);
    }

    /**
     * Обработчик событий
     *
     * @param e событие
     */
    @Override
    public void accept(Event e) {
        // если событие - это закрытие окна
        if (e instanceof EventWindowClose) {
            // завершаем работу приложения
            App.terminate();
        } else if (e instanceof EventWindowCloseRequest) {
            window.close();
        } else if (e instanceof EventFrameSkija ee) {
            Surface s = ee.getSurface();
            paint(s.getCanvas(), new CoordinateSystem2i(s.getWidth(), s.getHeight()));
        }
    }

    /**
     * Рисование
     *
     * @param canvas   низкоуровневый инструмент рисования примитивов от Skija
     * @param windowCS СК окна
     */
    public void paint(Canvas canvas, CoordinateSystem2i windowCS) {
        // запоминаем изменения (пока что там просто заливка цветом)
        canvas.save();
        // очищаем канвас
        canvas.clear(APP_BACKGROUND_COLOR);
        // рисуем заголовок
        label.paint(canvas, windowCS);
        // рисуем второй заголовок
        label2.paint(canvas, windowCS);
        // рисуем третий заголовок
        label3.paint(canvas, windowCS);
        // восстанавливаем состояние канваса
        canvas.restore();
    }
}