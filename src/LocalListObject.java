
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.*;
import javax.swing.*;

/* Класс с одним Методом, который организует Список Объектов, который он считывает из текстового файла, расположенного
 * на компьютере. Т.е. по адресу текстового файла читает его и формирует из него Список Объектов (Игр). Применяется
 * в главном методе в Классе MyGameSearcher при формировании Массива Объектов */

public class LocalListObject {

	private static int ind; // это переменная для вывода номера выбранного элемента из приемника
	
	/////////////////////////////// Поля //////////////////////////////////////

	private List<GameClass> GameList; // список Игр

	private List<String> FieldList;   // список отсортированный по Полю (Издатели, Мапперы и др.)

	int level; // уровень сортировки
	int starCode; // наличие введенного кода *_ _
	int on_off_inet; // наличие интернета, 0 - нет, 1 - есть

	String fieldName; // для вводимой с клавы доп инфы
	String searchTrace; // для отслуживания "истории" поиска
	String key; // то, по чему сортируем список

	///////////////////////////// Конструкторы ////////////////////////////////////////

	/*
	 * 1. Конструктор для использования при вводе "InetOff" - сильно еще не пробовал
	 */

	LocalListObject() {

		level = 0;
		starCode = 0;
		on_off_inet = 0;

		fieldName = "";
		searchTrace = "All Games";
		key = "All Games";

		GameList = new ArrayList<GameClass>();
		FieldList = new ArrayList<String>();
	}

	/*
	 * 2. Конструктор для использования при вводе "InetOn" - сильно еще не пробовал
	 */

	LocalListObject(byte a) {

		level = 0;
		starCode = 0;
		on_off_inet = 1;

		fieldName = "";
		searchTrace = "All Games";
		key = "All Games";

		GameList = new ArrayList<GameClass>();
		FieldList = new ArrayList<String>();
	}

	/*
	 * 3. Конструктор для формирования Полного Объекта. Использую при первоначальном
	 * формировании Объекта и при вводе ""
	 */

	LocalListObject(AddressManager manag) {

		level = 1;
		starCode = 0;
		on_off_inet = 0;

		fieldName = "";
		searchTrace = "All Games";
		key = "All Games";

		GameList = readGamesFromFile(manag);
		FieldList = new ArrayList<String>();
	}

	/*
	 * 4. Конструктор использую в InputAnalyse, в дефолтном варианте (когда нет
	 * совпадений по Полям и др.)
	 */

	LocalListObject(List<GameClass> a, int b, String c, String d) {

		level = b;
		starCode = 0;
		on_off_inet = 0;

		fieldName = "";
		searchTrace = d;
		key = c;

		GameList = a;
		FieldList = new ArrayList<String>();
	}

	/*
	 * 5. Конструктор использую в InputAnalyse, когда есть совпадение по Полям
	 * (creators, mappers, years)
	 */

	LocalListObject(List<GameClass> a, String c, String b, int d) {

		level = d;
		starCode = 0;
		on_off_inet = 0;

		fieldName = c;
		searchTrace = b;
		key = "";

		GameList = a;
		FieldList = ServiceMethods.getFieldListNew(a, c);
	}

	/* Getter-ы */

	public List <GameClass> getGameList () {
		
		return this.GameList;
	}
	
	public List <String> getFieldList () {
		
		return this.FieldList;
	}
	
	public String getKey() {

		return key;
	}

	public int getLevel() {

		return level;
	}

	/* Setter-ы (пока не использую, т.к. все поля открыты) */

	public void setAddInput(String k) {

		this.fieldName = k;
	}

	public void setKey(String k) {

		this.key = k;
	}

	/* 1. Наш метод который будет считывать данные, формировать Коллекцию Игр и
	  сортировать ее применяя наш Компаратор (это вконце) из Класса MyComp
	  аргументом на место filename должен передаваться адрес текстового файла
	  Применяю в Конструкторе №3 */

	private List<GameClass> readGamesFromFile(AddressManager manag) {

		List<GameClass> games = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(new FileReader(manag.fileAddress))) {

			/*
			 * заводим 3 переменные, перед циклом, значит они "глобальные" по отношению к
			 * циклу. В result будем объеденять строки, относящиеся к одному объекту
			 */

			String line;
			String result = "";
			int count = 0;

			/*
			 * каждая итерация цикла это чтение одной стоки. Соответственно, ставим условия
			 * и тем самым что-то игнорируем и начинаем цикл заново, что-то обрезаем и т.д.
			 */

			while ((line = br.readLine()) != null) {

				// считываем Строку без учета пробелов вначале или вконце

				line = line.trim();

				// если Строка пустая или начинается с // (но не заканчивается на "*/") -
				// пропускаем ее

				if (line.isEmpty() || (line.startsWith("//") && (!line.endsWith("*/")))) {
					continue;
				}

				///////////// Блок, реализующий игнорирование кода в /* .... */ /////////////

				if ((line.startsWith("/*")) && (line.endsWith("*/"))) {

					continue;
				}

				if (line.startsWith("/*")) {

					count = 1;
					continue;
				}

				if (((count == 1) && (line.endsWith(","))) ||

						((count == 1) && (line.endsWith(";"))) ||

						((count == 1) && (line.isEmpty())) ||

						((count == 1) && (line.startsWith("//")) && (!line.endsWith("*/")))) {

					continue;
				}

				if ((count == 1) && (line.endsWith("*/"))) {

					count = 0;
					continue;
				}

				///////////////////////////////////////////////////////////////////////////////

				// если Строка кончается на "," значит дальше будет адрес Фотки, надо объеденять
				// строки

				if (line.endsWith(",")) {
					result += line.trim() + " ";
					continue;
				}

				// если Строка кончается на ";" значит это окончательный конец строки, надо
				// заканчивать объеденение

				if (line.endsWith(";")) {
					line = line.substring(0, line.length() - 1); // удаляем эти ;
					result += line.trim() + " ";
					line = result;
					result = "";
					// continue;
				}

				/*
				 * разделяем Строку на подстроки, "," - разделитель и упаковываем их в Массив
				 * Строк
				 */
				/*
				 * т.е создаем Массив Строк, в котором каждый элемент (каждая строка) это
				 * значение нашего Поля передаваемое Конструктору. В зависимости от количества
				 * полей (переменная h), выбирается соответствующий Конструктор, их всего 6
				 */

				String[] parts = line.split(",");

				int h = parts.length; // переменная для отслеживания количества Полей считанных для одного Объекта.

				// в каждой Подстроке удаляем Пробелы (если были) вначале или вконце

				for (int i = 0; i < h; i++) {
					parts[i] = parts[i].trim();
				}

				// если Подстрока начинается с ", удаляем этот символ

				for (int i = 0; i < h; i++) {
					if (parts[i].startsWith("\"")) {
						parts[i] = parts[i].substring(1);
					}
				}

				// если Подстрока кончается на ", удаляем этот символ

				for (int i = 0; i < h; i++) {
					if (parts[i].endsWith("\"")) {
						parts[i] = parts[i].substring(0, parts[i].length() - 1);
					}
				}

				///////////////////////// блок формирования Объектов из считаных строк
				///////////////////////// ////////////////////

				/*
				 * в зависимости от количества считанных частей (h) применяем соответствующий
				 * Конструктор
				 */

				if (h == 1) { // 3 фотки по умолчанию

					String name = parts[0];

					GameClass person = new GameClass(manag, name);
					games.add(person);
				}

				else if (h == 2) { // 3 фотки по умолчанию

					String name = parts[0];
					String creator = parts[1];

					GameClass person = new GameClass(manag, name, creator);
					games.add(person);
				}

				else if (h == 3) { // 3 фотки по умолчанию

					String name = parts[0];
					String creator = parts[1];
					String year = parts[2];

					GameClass person = new GameClass(manag, name, creator, year);
					games.add(person);
				}

				else if (h == 4) { // 3 фотки по умолчанию

					String name = parts[0];
					String creator = parts[1];
					String mapper = parts[2];
					String year = parts[3];

					GameClass person = new GameClass(manag, name, creator, mapper, year);
					games.add(person);
				}

				else if (h == 5) { // 3 фотки по умолчанию

					String name = parts[0];
					String creator = parts[1];
					String mapper = parts[2];
					String year = parts[3];
					String comment = parts[4];

					GameClass person = new GameClass(manag, name, creator, mapper, year, comment);
					games.add(person);
				}

				/*
				 * благодаря этому блоку else стало возможно просматривать бесконечное число
				 * фоток одной игры. Т.е. сколько фоток будет столько и прочитаем
				 */

				else { // 1 и более кастомных фотки

					String name = parts[0];
					String creator = parts[1];
					String mapper = parts[2];
					String year = parts[3];
					String comment = parts[4];

					String[] pik = new String[h - 5]; // создаем массив строк для Адресов фоток

					/* заполняем Массив pik только Адресами фоток, считанных из файла */

					for (int i = 5; i < parts.length; i++) {

						pik[i - 5] = manag.photoFolderAddress + parts[i];
					}

					/* создаем объект используя "универсальный" конструктор */

					GameClass person = new GameClass(manag, name, creator, mapper, year, comment, pik);
					games.add(person); // добавляем объект в список games
				}

			} // конец цикла while

			/*
			 * пробовал вводить это условие по аналогии с закрытием потока при чтени инфы из
			 * интернета
			 */

			// br.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		// вызываем Метод sort и передаем ему наш Компаратор, это сортирует нашу
		// Коллекцию (либо по Алфавиту либо по Году выпуска)

		games.sort(new MyNameCompNew());
		// games.sort(new MyYearComp());

		return games;

	} // конец Метода

	/* 2. Метод принимает от пользователя Фразу и обрабатывает ее, выбирая тот или
	 * иной Конструктор, т.е. формируя объект с новыми данными в полях и новыми
	 * списками */

	public LocalListObject inputAnalyse(AddressManager manag) {

		/* если список Игр пуст, значит ничего не нашлось, а значит нужно
		 * обновлять список - используем Конструктор №3 */
		
		if (this.GameList.size()==0) {
			
			//LocalListObject list = new LocalListObject(GameClass.fileAddres);
			LocalListObject list = new LocalListObject(manag);
			
			return list;
		}

		/* вводим что-либо с клавиатуры */
		
		key = JOptionPane.showInputDialog(null, searchTrace + "->", "Input Window",
				JOptionPane.QUESTION_MESSAGE); // вводим слово в окно

		/* записываем введенное с клавы в "историю поиска" */

		searchTrace = searchTrace + "->" + key;

		/* Первые 3 условия, сформируют в объекте еще один список с уникальными названиями Издателей,
		 * Мапперов либо Годов, присутствующий в основном списке */
		
		// 1. если вводим creators или "издатели" - в addInput пишем "creator"

		if (key.equals("creators") || (key.equals("издатели"))) {

			/* используем Конструктор № 5 */
			
			LocalListObject list = new LocalListObject(this.GameList, "creator", searchTrace, level);

			return list;
		}

		// 2. если вводим "mappers" или "мапперы" - в addInput пишем "mapper"

		else if (key.equals("mappers") || (key.equals("мапперы"))) {

			/* используем Конструктор № 5 */
			
			LocalListObject list = new LocalListObject(this.GameList, "mapper", searchTrace, level);

			return list;
		}

		// 3. если вводим "years", "года" или "годы" - в addInput пишем "year"

		else if (key.equals("years") || (key.equals("года")) || (key.equals("годы"))) {

			/* используем Конструктор № 5 */
			
			LocalListObject list = new LocalListObject(this.GameList, "year", searchTrace, level);

			return list;
		}

		/* Следующие 2 условия (4 и 5) заполняют поле on_off_inet, которое дальше будет использоваться
		 * для отключения либо включения поиска в Инете либо на Компьютере */
		
		/* 4. если вводим "InetOff" переменная webOrNot = 1 и как-бы отключаем закачку
		 * даных из Инета */

		else if (key.equals("InetOff")) {

			/* используем Конструктор № 1 */
			
			LocalListObject list = new LocalListObject();

			return list;
		}

		/* 5. если вводим "InetOn" переменная webOrNot = 0 и как-бы включаем закачку
		 * даных из Инета */

		else if (key.equals("InetOn")) {

			byte a = 1;

			/* используем Конструктор № 2 */
			
			LocalListObject list = new LocalListObject(a);

			return list;
		}

		/* 6. Условие по обновлению Списка до полного */
		
		else if ( key.equals("") ) {

			/* используем Конструктор № 3 */
			
			LocalListObject list = new LocalListObject(manag);

			return list;
		}

		/* 7. Условие по заполнению поля starCode и последующему выбору по этому полю
		 * одного элемента из Списка */
		
		else if (ServiceMethods.isStarCodeOk(key)) {

			/* если мы ввели старКод, а поле "Издателей" заполнено, то
			 * по старКоду выбираем из списка "Издателей", а возвращаем объект
			 * со списком Игр, отсортированным по выбранному элементу из списка
			 * "Издателей" */
			
			if ( !this.fieldName.equals("") ) {
				
				/* переводим старКод в цифру */
				
				int ku = Integer.parseInt(key.substring(1, key.length()));
				
				/* присваиваем key значение "издателя" под номером старКода */
				
				this.key = this.getFieldList().get(ku-1);

				searchTrace = searchTrace + "->" + key;

				/* далее идет "стандартная" сортировка списка Игр по key */
				
				LocalListObject xlist = this.objectSorting();
				
				return xlist;
				
			} else {
			
				/* это случай, когда по старКоду выбираем из Списка Игр, т.е.
				 * когда список "Издателей" был пуст */
				
				this.starCode = Integer.parseInt(key.substring(1, key.length()));
				this.key = "";

				return this;		
			}
		}

		/* 8. Дефолтное условие, если предидущие условия не выполены, значит просто
		 * проводим Сортировку */
		
		else {

			LocalListObject list = objectSorting();

			return list;
		}

	} // конец метода inputAnalyse

	/* 3. Метод сортирует Список Объекта и вызвращает Объект с отсортированным
	 * Списком Игр */

	private LocalListObject objectSorting() {

		int w = 0; // переменная для отслеживания того, какой список возвращаем

		/* создадим Список, который и будет возвращать Метод */

		List<GameClass> objectList = new ArrayList<GameClass>();

		this.level++; // вызываем этот метод и t+1 сразу - для отображения этапа Сортировки

		/* при вводе "allGames" поиск уже не производим, возвращаем объект как есть, так
		 * как метод по анализу ввода (inputAnalyse) заполнил это поле, а значит и
		 * вернул полный список, т.е. в этом объекте присутствует полный список */

		if (this.key.equals("All Games")) {

			searchTrace = "All Games";

			return this;
		}

		/* если и объекта заполненно поле fieldName, значит его не нежно сортировать, у
		 * него присутствует список Издателей или Мапперав и т.е. - возвращаем как
		 * есть */

//		if (!this.fieldName.equals("")) {
//
//			return this;
//		}

		/* если вводим стар код - номер идет в поле starCode, и объект возвращается как
		 * есть */

		if (this.starCode != 0) {

			return this;
		}

		/* на данный момент у нас key - это то, что ввели с клавиатуры. Здесь можно
		 * передать наше key в спец метод, в котором это значение обработается и
		 * проверится на соответ- ствие условиям: - начинается ли оно с "*" - содержит
		 * ли слова "creators", "mappers" и т.д. т.е. здесь будет аналог блока
		 * "блок ввода спец. команд в начальное окно ввода" из старого Класса
		 * MyGameSearch */

		///////////////////// сортировка по полному совпадению по Полям /////////////////////

		// проходим по списку, и собираем в отдельный список Объекты, которые
		// соответствуют условиям

		for (int i = 0; i < GameList.size(); i++) {

			if ((this.key.equals(GameList.get(i).getCreator())) || (this.key.equals(GameList.get(i).getMapper()))
					|| (this.key.equals(GameList.get(i).getYear()))) {

				objectList.add(GameList.get(i)); // добавляем Объект с совпадением в наш Список

				w++;
			}
		}

		if (w > 0) {

			// searchTrace = searchTrace + "->" + key;

			LocalListObject list = new LocalListObject(objectList, level, this.key, searchTrace);

			return list;

		}
		// если что-то нашлось - возвращаем этот список

		/////////////////////////////////////////////////////////////////////////////////////////////////////

		/////////////////////////////////// Блок поиска непосредственно в Именах
		/////////////////////////////////// ////////////////////////////

		if (this.key.length() > 0) {

			char n = this.key.charAt(0); // n - первый символ введенного слова

			///////////////////// блок для сортировки по Одной введеной Букве
			///////////////////// ////////////////////////////

			/*
			 * если введенное слово - это Один символ и это Буква, то делаем эту букву
			 * Заглавной (даже если она и была заглавной) и отсортировываем объекты,
			 * названия которых начинается на эту Букву
			 */

			if ((this.key.length() == 1) && (Character.isAlphabetic(n))) {

				char n1 = Character.toUpperCase(n);

				this.key = String.valueOf(n1);

				for (int i = 0; i < GameList.size(); i++) {

					if (this.key.equals(String.valueOf(GameList.get(i).getName().charAt(0)))) {

						objectList.add(GameList.get(i)); // добавляем Объект с совпадением в наш Список

						w++;
					}
				}

				if (w > 0) {

					LocalListObject list = new LocalListObject(objectList, level, this.key, searchTrace);

					return list;

				}
				// если что-то нашлось - возвращаем этот список

				/////////////////////////////////////////////////////////////////////////////////////////////////////

				/////////////////////////////////// если больше одной буквы или символы
				/////////////////////////////////// /////////////////////////////

			} else {

				/* ищем введеное в названиях Игр и считаем кол-во найденого */

				for (int i = 0; i < GameList.size(); i++) {

					if (GameList.get(i).getName().contains(this.key)) {

						objectList.add(GameList.get(i)); // добавляем Объект с совпадением в наш Список

						w++;
					}
				}

				if (w > 0) {

					LocalListObject list = new LocalListObject(objectList, level, this.key, searchTrace);

					return list;

				}
				; // если что-то нашлось - возвращаем этот список

			}

		} // конец if (a.length()>0) - основного

		//////////////////////////////////////////////////////////////////////////////////////////////////////

		LocalListObject list = new LocalListObject(objectList, level, this.key, searchTrace);

		return list;

	} // конец Метода 2_objectSorting

	/* 4. Метод по выводу Списка GameClass в Окно (JOptionPane) со
	 * Слайдером и возможностью выбора элементов из этого списка Мышью.
	 * Вот над созданием этого метода (Окна) я бился долгие месяцы! */
	
	public LocalListObject mouseChooseWindow(AddressManager manag) {

		/* если метод видит "end" - ничего не показывает, и в main - конец проги */
		
		if (this.key.equals("end")) {

			return this;
		}

		/* если посмотрели спиок, а потом ввели starCode (*_ _) значит показываем фотки
		 * этой игры с индексом СтарКода (-1, т.к. индексация с 0) */
		
		if ( this.starCode!=0 ) {
			
			this.GameList.get(this.starCode-1).showPics(manag);
			
			this.starCode = 0;
			
			LocalListObject retList = new LocalListObject(manag);

			return retList;			
		}

		/* создаем Список под строки: либо названий Игр, либо Издателей */
		
		List <String> y = new ArrayList<String>();
		
		/* если поле fieldName заполненно, берем этот список */
		
		if (!this.fieldName.equals("")) {
			
			for (int i = 0; i < this.getFieldList().size(); i++) {

				y.add(this.getFieldList().get(i));
			}
			
		/* если поле fieldName пусто, берем список Названий Игр */
			
		} else {
		
			// создаем Массив Строк "y" из нашего Массива Объектов

			for (int i = 0; i < this.getGameList().size(); i++) {

				y.add(this.getGameList().get(i).getName());
			}
		}
		
		// добавляем наш Массив Строк 'y' в наш JList

		DefaultListModel<String> listModel = new DefaultListModel<>();

		// добавляем наш Массив Строк в наш JList

		for (String item : y) {

			listModel.addElement(item);
		}

		// создаем JList - myList

		JList<String> myList = new JList<>(listModel);

		// создаем типо "Счетчик" к нашму JList

		myList.setCellRenderer(new NumberedListCellRenderer());

		// создаем Панель Прокрутки и добавляем туда наш Список myList

		JScrollPane scrollPane = new JScrollPane(myList);

		/* создадим Списки list и clist для передачи их далее в приемник, т.к. если просто взять
		 * их из объекта this.getGameList - в приемнике их не видно (this) */
		
		List <GameClass> list = this.getGameList();
		List <String>   clist = this.getFieldList();
			
		////////////////////////// Регистрация Приемника ///////////////////////////////

		myList.addListSelectionListener(new ListSelectionListener() { // начало Метода регистрации Приемника

			public void valueChanged(ListSelectionEvent e) { // начало Метода регистрации События
				
				if (!e.getValueIsAdjusting()) {

					// получаем один выбранный Мышкой Элемент (Строку)

					String selectedElement = myList.getSelectedValue();

					// получаем Индекс (номер выбранной Строки)

					int index = -1;

					ListModel<String> model = myList.getModel();

					/* при выборе мышью ввел минимальное значение i = 1, даже если выбираем
					 * первый элемент (i=0), i будет равно 1, тем самым будет видно, что
					 * выбор сделан мышью, потому что если ничего не выбираем, i=0, а почему
					 * я не понял, думал, что должно быть -1. Поэтому далее при использовании
					 * index и ind вычитаю 1 */
					
					for (int i = 0; i < model.getSize(); i++) {
						
						if (model.getElementAt(i).equals(selectedElement)) {
							index = i+1; // ввел +1 для отслеживания выбора мышью
							break;
						}
					}
				
					/* если список clist ("Издателей") пуст, значит перед нами список Игр,
					 * заначит, выбираем игру по индексу и показываем ее фотки */
					
				  //if ( clist.size()==0 ) { // здесь почеу-то работало и так..
					if ( clist.size()==0 && index!=0) {
						
						// по Индексу выбранной Строки, выбираем Объект из нашего Массива Объектов

						GameClass shit = list.get(index-1);

						shit.showPics(manag); // показываем фотографии Объекта (игры)
						
					/* в обратном случае - в списке "Издателей" что-то есть, а значит берем индекс,
					 * пишем в глобальную переменную и закрываем это окно */
						
					} else {
						
						ind = index;	
						
						/* Эту часть подсказал GPT, это мгновенно закрывает Окно при нажатии на один
						 * из элементов Списка */
						
						Window window = SwingUtilities.getWindowAncestor (myList);
						
						if (window instanceof JDialog) {
							JDialog dialog = (JDialog) window;
							dialog.dispose();
						}
						
						else if (window instanceof JFrame) {
							JFrame frame = (JFrame) window;
							frame.dispose();
						}
					}
	
			///////////////////////////////////////////////////////////////////////////////////////////////////
					
				} // конец if
				
			} // конец метода Внутреннего Абстрактного Класса, т.е. конец Метода регистрации События
			
		}); // конец нашего Внутреннего Абстрактного Класса, т.е. Метода регистрации Приемника

		//ServiceMethods.windowShow(Integer.toString(ind)); // смотрел значение ind
		/* почему-то если ничего не выбираем мышью из списка, ind равно 0, а я думал лолжно быть -1 */
		
		/////////////////////// Конец Регистрации Приемника //////////////////////////////

		scrollPane.setPreferredSize(new Dimension(500, 500));

		JOptionPane.showMessageDialog(null, scrollPane, this.searchTrace, JOptionPane.PLAIN_MESSAGE);
		
		/* после показа списка, если в нем 1 Игра, показываем ее без лишних движений */
	
		if (this.GameList.size()==1) {
			
			this.GameList.get(0).showPics(manag);
			
			LocalListObject retList = new LocalListObject(manag);

			return retList;
		}

		/* наш метод вызвращает объект нашего Класса, вот это блок по возвращению:
		 * 
		 * если Список "Издателей" был, то в key записываем то, что выбрали мышью,
		 * обновляем "историю" поиска searchTrace, обнуляем статическую ind и
		 * формируем объект для возвращения - этот объект будет с отсортированным
		 * списком Игр по переданому key и он сразу выведет на экран список Игр этим
		 * же методом, т.к. список "Издателей" у него уже пуст и вернет этот объект */
		
		if ( !this.fieldName.equals("") && ind!=0) {
			
			this.key = this.getFieldList().get(ind-1);

			searchTrace = searchTrace + "->" + key;
			
			ind = 0; 

			LocalListObject xlist = this.objectSorting().mouseChooseWindow(manag);
			
			return xlist;
			
		/* если же в Объекте не было списка "Издателей", метод после показа (и выбора
		 * мышью) Списка Игр, вернет этот же Объект */
			
		} else {
			
			return this;
		}

	} // конец Метода mouseChooseWindow()
	
	/* 5. Метод для чека - что содержит наш Оьъект */

	public void getInfo() {
		
		if (this.key.equals("end") ) return;

		String text = "Level: " + level + "\n"

				+ "Key: " + key + "\n" + "fieldName: " + fieldName + "\n" + "searchTrace: " + searchTrace + "\n"
				+ "starCode: " + starCode + "\n" + "on_off_inet: " + on_off_inet + "\n" + "GameListSize: "
				+ GameList.size() + "\n" + "FieldListSize: " + FieldList.size() + "\n";

		JTextArea textArea = new JTextArea(text);
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setPreferredSize(new Dimension(600, 500));

		JOptionPane.showMessageDialog(null, scrollPane, "That's what we have here: ",
				JOptionPane.PLAIN_MESSAGE);
	}

} // конец Класса LocalListObject
