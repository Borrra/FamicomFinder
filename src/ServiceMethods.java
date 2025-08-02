import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.lang.reflect.Field;


/* В данном файле 4 Класса: Первый - основной и остальные 3 вспомогательные */

////////////////////////// ПЕРВЫЙ КЛАСС /////////////////////////////

/* В этом классе будут рутинные (служебные Методы) для моего проекта */

public class ServiceMethods {

	/* 10 Методов windowShow/windowShoww по показу даных и 4 разных:
	 * 
	 * 1.  windowShow (String arr);
	 * 2.  windowShow (String arr, String akk);
	 * 3.  windowShow (String[] arr);
	 * 4.  windowShow (String[] arr, String a);
	 * 5.  windowShoww(List<String> arr);
	 * 6.  windowShoww(List<String> arr, String a);
	 * 7.  windowShow (GameClass[] arrg);
	 * 8.  windowShow (List<GameClass> arrg);
	 * 9.  windowShow (GameFolderClass[] arrg, String p); вывод всей инфы
	 * 10. windowShoww(GameFolderClass[] arrg, String p); вывод только назв. Папки
	 * 
	 * 11. informWindow();
	 * 12. List<String> getFieldListNew(List<GameClass> objects, String fieldName);
	 * 13. boolean isStarCodeOk(String k);
	 * 14. int yesNoWindow(); */

	// 1. Метод выводит в Окно передаваюмую ему Строку

	public static void windowShow(String arr) {

		String text = arr;
		String title = "Let's see what we got here:";

		JTextArea textArea = new JTextArea(text);
		JScrollPane scrollPane = new JScrollPane(textArea);

		scrollPane.setPreferredSize(new Dimension(600, 100));

		JOptionPane.showMessageDialog(null, scrollPane, title, JOptionPane.PLAIN_MESSAGE);

	} // конец Метода № 1

	/* 2. Метод выводит в Окно передаваюмую ему Строку и вторую передаваемую ему
	 * Строку он выводит в заголовок Окна */

	public static void windowShow(String arr, String akk) {

		String text = arr;
		String title = akk;

		JTextArea textArea = new JTextArea(text);
		JScrollPane scrollPane = new JScrollPane(textArea);

		scrollPane.setPreferredSize(new Dimension(600, 100));

		JOptionPane.showMessageDialog(null, scrollPane, title, JOptionPane.PLAIN_MESSAGE);

	} // конец Метода № 2

	// 3. Метод выводит в Окно списком Массив Строк

	public static void windowShow(String[] arr) {

		String text = "";
		String title;

		for (int i = 0; i < arr.length; i++) {

			String ch = String.format("%03d", i + 1);
			text = text + ch + ". " + arr[i] + "\n";
		}

		title = "There're " + arr.length + " elements we have";

		JTextArea textArea = new JTextArea(text);
		JScrollPane scrollPane = new JScrollPane(textArea);

		scrollPane.setPreferredSize(new Dimension(600, 500));

		JOptionPane.showMessageDialog(null, scrollPane, title, JOptionPane.PLAIN_MESSAGE);

	} // конец Метода № 3

	// 4. Метод выводит в Окно списком Массив Строк и Заголовок Окна

	public static void windowShow(String[] arr, String a) {

		String text = "";
		String title;

		for (int i = 0; i < arr.length; i++) {

			String ch = String.format("%03d", i + 1);
			text = text + ch + ". " + arr[i] + "\n";
		}

		title = arr.length + " elements. " + a;

		JTextArea textArea = new JTextArea(text);
		JScrollPane scrollPane = new JScrollPane(textArea);

		scrollPane.setPreferredSize(new Dimension(600, 500));

		JOptionPane.showMessageDialog(null, scrollPane, title, JOptionPane.PLAIN_MESSAGE);

	} // конец Метода № 4

	// 5. Метод выводит в Окно списком Список List <String>

	public static void windowShoww(List<String> arr) {

		String text = "";
		String title;

		for (int i = 0; i < arr.size(); i++) {

			String ch = String.format("%03d", i + 1);
			text = text + ch + ". " + arr.get(i) + "\n";
		}

		title = "There're " + arr.size() + " elements we have";

		JTextArea textArea = new JTextArea(text);
		JScrollPane scrollPane = new JScrollPane(textArea);

		scrollPane.setPreferredSize(new Dimension(600, 500));

		JOptionPane.showMessageDialog(null, scrollPane, title, JOptionPane.PLAIN_MESSAGE);

	} // конец Метода № 5

	// 6. Метод выводит в Окно списком Список List <String> и Заголовок Окна

	public static void windowShoww(List<String> arr, String a) {

		String text = "";
		String title;

		for (int i = 0; i < arr.size(); i++) {

			String ch = String.format("%03d", i + 1);
			text = text + ch + ". " + arr.get(i) + "\n";
		}

		title = arr.size() + " elements. " + a;

		JTextArea textArea = new JTextArea(text);
		JScrollPane scrollPane = new JScrollPane(textArea);

		scrollPane.setPreferredSize(new Dimension(600, 500));

		JOptionPane.showMessageDialog(null, scrollPane, title, JOptionPane.PLAIN_MESSAGE);

	} // конец Метода № 6

	/* 7. Метод по выводу Имен Массива Объектов GameClass[] в Окно (JOptionPane) со
	 * Слайдером в заголовок передаются Статич. переменные чтобы значть что ищем  */

	public static void windowShow(GameClass[] arrg) {

		String text = "";
		String title;

		for (int i = 0; i < arrg.length; i++) {

			String ch = String.format("%03d", i + 1);
			text = text + ch + ". " + arrg[i].getName() + "\n";
		}

		// формируем надпись в заголовке Окна используя Статич. Переменные j и k

		title = "There're " + arrg.length + " elements found. Serching for ";

		if (arrg.length == 0) {

			text = "\n\tNothing is found, dude!";
		}

		JTextArea textArea = new JTextArea(text);
		JScrollPane scrollPane = new JScrollPane(textArea);

		scrollPane.setPreferredSize(new Dimension(600, 500));

		JOptionPane.showMessageDialog(null, scrollPane, title, JOptionPane.PLAIN_MESSAGE);

	} // конец метода № 7

	// 8. Метод по выводу Имен Списка Объектов List <GameClass> в Окно (JOptionPane) со Слайдером

	public static void windowShow(List<GameClass> arrg) {

		String text = "";
		String title;

		for (int i = 0; i < arrg.size(); i++) {

			String ch = String.format("%03d", i + 1);
			text = text + ch + ". " + arrg.get(i).getName() + "\n";
		}

		// формируем надпись в заголовке Окна используя Статич. Переменные j и k

		title = "There're " + arrg.size() + " elements found.";

		if (arrg.size() == 0) {

			text = "\n\tNothing is found, dude!";
		}

		JTextArea textArea = new JTextArea(text);
		JScrollPane scrollPane = new JScrollPane(textArea);

		scrollPane.setPreferredSize(new Dimension(600, 500));

		JOptionPane.showMessageDialog(null, scrollPane, title, JOptionPane.PLAIN_MESSAGE);

	} // конец метода № 8

	/* 9. Метод по выводу в Окно списка всех Папок и адресов Фоток в них */

	public static void windowShow(GameFolderClass[] arrg, String p) {

		String text = "";
		String title;

		for (int i = 0; i < arrg.length; i++) {

			String ch = String.format("%03d", i + 1);
			text = text + ch + ". " + arrg[i].getName() + "\n";

			for (int k = 0; k < arrg[i].getPhotoNames().length; k++) {

				text = text + arrg[i].getPhotoNames()[k] + "\n";
			}

			text = text + "\n";
		}

		// формируем надпись в заголовке Окна используя Статич. Переменные j и k

		title = "There're " + arrg.length + " elements. " + p;

		JTextArea textArea = new JTextArea(text);
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setPreferredSize(new Dimension(500, 500));

		JOptionPane.showMessageDialog(null, scrollPane, title, JOptionPane.PLAIN_MESSAGE);

	} // конец Метода № 9

	/* 10. Метод по выводу в Окно только Имен папок  */

	public static void windowShoww(GameFolderClass[] arrg, String p) {

		String text = "";
		String title;

		for (int i = 0; i < arrg.length; i++) {

			String ch = String.format("%03d", i + 1);
			text = text + ch + ". " + arrg[i].getName() + "\n";
		}

		// формируем надпись в заголовке Окна используя Статич. Переменные j и k

		title = "There're " + arrg.length + " elements. " + p;

		JTextArea textArea = new JTextArea(text);
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setPreferredSize(new Dimension(500, 500));

		JOptionPane.showMessageDialog(null, scrollPane, title, JOptionPane.PLAIN_MESSAGE);

	} // конец Метода № 10
	
	/* 11. Метод по выводу инфрмации по использованию Приложения */

	public static void informWindow() {

		String text = "       В окне моего поисковика можно вводить названия игр, либо просто символы\n"
				+ " содержащиеся в названии игры (если введена одна буква, программа выдаст все\n"
				+ " игры начинающиеся на эту букву) плюс при введении конкретного имени Издателя\n"
				+ " игры либо Маппера либо Года выпуска, также будет выведен список игр относящихся\n"
				+ " к данному Издателю, Мапперу или Году.\n"
				+ "       Также существует ряд ключевых слов с определенными функциями:\n\n"
				+ " creators                  - вывод списка всех Издателей\n" + " издатели\n\n"
				+ " mappers                 - вывод списка всех Мапперов\n" + " мапперы\n\n"
				+ " years                      - вывод списка всех Годов выпуска\n" + " года или годы\n\n"
				+ " InetOff                    - включаем работу с Компьютера\n\n"
				+ " InetOn                    - включаем работу с Интернета\n\n"
				+ " refreshFile              - обновляем Текстовый Файл\n\n"
				+ " refreshPhoto          - обновляем Папку с Фотками\n\n"
				+ " refreshManag         - обновляем данные по файлам\n\n"
				+ " managInfo              - смотрим состав Менеджера файлов\n\n"
				+ " listInfo                    - смотрим состав Списка\n\n"
				+ " Info                        - смотрим Провила Пользования Прогой\n\n"
				+ " end или конец       - выход из Программы\n";

		JTextArea textArea = new JTextArea(text);
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setPreferredSize(new Dimension(600, 500));

		JOptionPane.showMessageDialog(null, scrollPane, "Pay attention! Important Information!",
				JOptionPane.PLAIN_MESSAGE);

	} // конец Метода № 11

	/* 12 Экспериментальный Метод по получению Массива Строк Определенного Поля из Массива Объектов
	 * GameClass, название нужного Поля передается вторым Аргументом.
	 * Использую его в Конструкторе №5 класса GameListClass. */

	public static List<String> getFieldListNew(List<GameClass> objects, String fieldName) {

		List<String> list = new ArrayList<String>();

		Set<String> set = new TreeSet<>();

		for (int i = 0; i < objects.size(); i++) {

			try {

				Field field = GameClass.class.getDeclaredField(fieldName);
				field.setAccessible(true); // Allow access to private fields

				list.add((String) field.get(objects.get(i)));

			} catch (NoSuchFieldException e) {

				System.out.println("Field not found: " + fieldName);

				return list; // Return empty array if field is not found

			} catch (IllegalAccessException e) {

				e.printStackTrace();
				return list; // Return empty array if access fails
			}
		}

		set.addAll(list);

		list.clear();

		list.addAll(set);

		set.clear();

		return list;

	} // конец Метода № 12
	
	/* 13. Метод по обработке Кода "*_ _ _" по которому в Проекте происходит выбор
	 * чего-либо из списка. Принимает Строку и если она начинается с "*" и
	 * возвращает true (если это число) если оно соответствующей длины и не 0.
	 * Использую его в Методе InputAnalyse, в одном из условий */

	public static boolean isStarCodeOk(String k) {

		boolean w = false; // то, что возвращаем
		int e = 4; // максимальный разряд проверяемого числа включая '*' ( *999 )

		if (k.equals("")) { // вот без этог условия у меня была ошибка!

			// System.out.println("Here this shit!");

			return w;
		}

		char[] chr = k.toCharArray();

		if ((k.charAt(0) == '*') && (k.length() > 1 && (k.length() <= e)) && chr[1] != '0') {

			int r = 0;

			for (int i = 1; i < k.length(); i++) {

				if (Character.isDigit(chr[i])) {

					r++;
				}
			}

			if (r == k.length() - 1) {

				w = true;
			}
		}

		return w;

	} // конец Метода № 13

	/* 14. Простой Метод Да/Нет, возвращает 0 - если Да, 1 - если Нет, и -1 - если
	 * Крестик Использую в main для выбора скачивания или нет фоток с инета */

	public static int yesNoWindow(String b) {

		// Show a dialog with Yes and No options

		int response = JOptionPane.showConfirmDialog(null, b, "Выберите вариант",
				JOptionPane.YES_NO_OPTION);

		return response;
	}

} // конец 1-го Класса ServiceMethods

////////////////////////// ВТОРОЙ КЛАСС /////////////////////////////

/* Служебный Класс, для нумерования строк в Окнах, методов: 13, 14 */

class NumberedListCellRenderer extends DefaultListCellRenderer {

	@Override

	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {

		JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

		label.setText(String.format("%03d. %s", index + 1, value.toString()));

		return label;
	}
} // конец 2-го класса NumberedListCellRenderer

///////////////////////// ТРЕТИЙ КЛАСС /////////////////////////////

/* этот Класс myWindow формирует JPanel с возможностью нажатия клавишь, а Метод
 * в конце применяет эту JPanel в JOptionPane.showMessageDialog(null, myPanel,
 * a, JOptionPane.PLAIN_MESSAGE), и этот же метод (procesWindow) применяется
 * в окне длительности процесса Класс AddressManager Метод compFilesFinder() */

class myWindow extends JPanel implements ActionListener {

	/* т.е. мы как бы делаем свою Кастомную JPanel, которя применяет интерфейс
	 * ActionListener, для возможности использования кнопок */

/////////* устанавливаем поля нашего Класса SnakeGame *//////////////

	private final int CELL = 20;
	private final int WIDTH = 310; // ширина окна
	private final int HEIGHT = 30; // высота окна

	private Timer timer;
	private long startTime;

	private long elapsedTimer = 0;
	
	/* заводим Связанный Список для координат каждого Кубика Змеи */

	private LinkedList<Point> stuff = new LinkedList<Point>();

	/* Это такой большой Конструктор */
	
	public myWindow(String k) {

		stuff.add(new Point(0, 7)); // начальное положение Змеи
		stuff.add(new Point(WIDTH - 180, 7));
		stuff.add(new Point(WIDTH - 180 + CELL, 7));

		/* задаем Размеры Окна, причем высоту задаем с учетом размера нижней,
		 * дополнительной зоны отображения Очков SCORE_HIGHT */

		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setBackground(Color.BLACK);
		setFocusable(true);

		/* регистрируем Приемник */

		addKeyListener(new KeyAdapter() {

			/* Класс KeyEvent */

			@Override
			public void keyPressed(KeyEvent e) {

				/* метод getKeyCode() возвращает код нажатой кнопки (просто число),
				 * соответственно по этому коду уже срабатывают Кейсы - их может быть сколько
				 * угодно */

				switch (e.getKeyCode()) {

				/* если менять координаты точки в этом блоке, то она будет двигаться только по
				 * мере нажатия клавиши, поэтому при нажатии стрелок меняется только значение
				 * token, а уже в Методе Snake будет действия на этот token */

				case KeyEvent.VK_ESCAPE:

					System.exit(0);
					break;

				} // конец Switch

			} // конец keyPressed

		}); // конец регистрации Приемника

		timer = new Timer(64, this); // скорость движения кубиков в окне
		timer.start();
		startTime = System.currentTimeMillis();

	} // конец Конструктора

	//////* переориентированные Методы класа JPanel */////

	@Override
	protected void paintComponent(Graphics g) { // то, что будет отображаться в Окне
		super.paintComponent(g);

		g.setColor(Color.RED);

		for (int i = 0; i < stuff.size(); i++) {

			g.fillRect(stuff.get(i).x, stuff.get(i).y, CELL, CELL);
		}

		long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;

		g.drawString("Seconds: " + elapsedTime, WIDTH - 180, HEIGHT - 10);

		elapsedTimer = elapsedTime;
	}

	@Override
	public void actionPerformed(ActionEvent e) { // метод который и делает Экшен

		moveSnake();

		repaint();
	}

	/* 1. Метод формирующий двигающийся объект */
	
	public void moveSnake() { // метод будет как бы циклиться в actionPerformed

		stuff.addFirst(new Point(stuff.getFirst().x + CELL, stuff.getFirst().y));

		stuff.removeLast(); // убираем Хвост

		if (stuff.getFirst().x >= WIDTH) {
			stuff.getFirst().x = 0;
		}
	}

	/* 2. Метод используем в классе AddressManager в методе compFilesFinder ()
	 * для отображения длительности процесса */

	public static void procesWindow(String a) {

		/* активируем наше Кастомное Окно, оно является JPanel по сути */

		myWindow myPanel = new myWindow("stuff");

		JOptionPane.showMessageDialog(null, myPanel, a, JOptionPane.PLAIN_MESSAGE);

	}

} // конец 3-го Класса myWindow

////////////////////////// ЧЕТВЕРТЫЙ КЛАСС /////////////////////////////

/* Класс Окна-Консоли, где 2 метода: 1. выводящий само окно (startWindow)
 * и отправляющий инфу в это окно */

class consoleWindow {

	private static JTextArea textArea; // Area for displaying console output
	private static JTextField inputField; // Field for user input

	/*
	 * мое Консоль-Окно создается в Методе. Поэтому видимо оно и закрывается само,
	 * конда метод заканчивает свою работу
	 */

	/* 1. Метод используется в Методах основного класса № 7 и 8 */

	static void startWindow(String my) {

		// Create the main frame

		JFrame frame = new JFrame(my);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(620, 400); // ширина и высота соответственно
		frame.setLayout(new BorderLayout());

		// Create a JTextArea for console output

		textArea = new JTextArea();
		textArea.setEditable(false); // Make it non-editable
		JScrollPane scrollPane = new JScrollPane(textArea); // Add scrolling capability
		frame.add(scrollPane, BorderLayout.CENTER);

		// Create a JTextField for user input

		inputField = new JTextField();
		frame.add(inputField, BorderLayout.SOUTH);

		// Add action listener for the input field

		inputField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String input = inputField.getText(); // Get text from input field

				if (input.equalsIgnoreCase("end")) {
					
					toConsole("Closing the console...");

					frame.dispose();

					// System.exit(0);

				} else {

					toConsole(input); // Append to the console
				}

				inputField.setText(""); // Clear the input field
			}
		});

		// Set up the frame visibility
		frame.setVisible(true);

	} // конец Метода 1. startWindow

	/* 2. Метод используется в Методах основного класса № 5 и 6 */

	static void toConsole(String text) {

		textArea.append(text + "\n"); // Append text with a newline

		textArea.setCaretPosition(textArea.getDocument().getLength()); // Scroll to bottom

//        if (text.equals("end")) {
//        	
//        	toConsole("\nClosing the console...");
//        	ServiceMethods.windowShow("the program is closing");
//        	
//        	System.exit(0);
//        	 
//        }

	} // конец метода 2. toConsole

} // конец 4-го Класса consoleWindow
