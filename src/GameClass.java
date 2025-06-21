
import java.util.*;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;

/* Класс в котором расположено описание Полей Игры, т.е. Поля и Конструкторы по формированию
 * наший Объектов (Игр) а также Методы по обработке наших Объектов. Это и Поиск и Сортировка,
 * вывод на экран Фотографий из папок и из Интернета. Также содержит статические переменные
 * с агресами файлов проекта. Нужно будет соствить подроброе содержание этого Класса */

public class GameClass {

	/////////////////// Данные (Поля) моего Класса (10 штук) / /////////////////////////

	// 7 основных

	private String name;
	private String creator;
	private String mapper;
	private String year;
	private String comment;
	private int amount;

	private String[] pics;

	// 3 внутренних (использую только в Конструкторах)

	private String addr1 = "1. General/1.jpg"; // Файл Фоток по умолчанию
	private String addr2 = "1. General/2.jpg";
	private String addr3 = "1. General/3.jpg";

	/////////////////// Конструкторы моего Класса (6 штук) /////////////////////////

	// 1. конструктор, если вводишь только Название (+ 3 дефолтных фотки)

	public GameClass(AddressManager manag, String name) {

		this.name = name;
		this.creator = "no name";
		this.mapper = "unknown";
		this.year = "19**";
		this.comment = "stay sharp";
		this.amount = 3;
		this.pics = new String[] { manag.photoFolderAddress + addr1, manag.photoFolderAddress + addr2, manag.photoFolderAddress + addr3 };
	}

	// 2. конструктор, если вводишь Название и Издателя (+ 3 дефолтных фотки)

	public GameClass(AddressManager manag, String name, String creator) {

		this.name = name;
		this.creator = creator;
		this.mapper = "unknown";
		this.year = "19**";
		this.comment = "stay sharp";
		this.amount = 3;
		this.pics = new String[] { manag.photoFolderAddress + addr1, manag.photoFolderAddress + addr2, manag.photoFolderAddress + addr3 };
	}

	// 3. конструктор, если вводишь Название, Издателя и Год (+ 3 дефолтных фотки)

	public GameClass(AddressManager manag, String name, String creator, String year) {

		this.name = name;
		this.creator = creator;
		this.mapper = "unknown";
		this.year = year;
		this.comment = "stay sharp";
		this.amount = 3;
		this.pics = new String[] { manag.photoFolderAddress + addr1, manag.photoFolderAddress + addr2, manag.photoFolderAddress + addr3 };
	}

	// 4. конструктор, если вводишь Название, Издателя, Маппер и Год (+ 3 дефолтных
	// фотки)

	public GameClass(AddressManager manag, String name, String creator, String mapper, String year) {

		this.name = name;
		this.creator = creator;
		this.mapper = mapper;
		this.year = year;
		this.comment = "stay sharp";
		this.amount = 3;
		this.pics = new String[] { manag.photoFolderAddress + addr1, manag.photoFolderAddress + addr2, manag.photoFolderAddress + addr3 };
	}

	// 5. конструктор при вводе всех полей (+ 3 дефолтных фотки)

	public GameClass(AddressManager manag, String name, String creator, String mapper, String year, String comment) {
		this.name = name;
		this.creator = creator;
		this.mapper = mapper;
		this.year = year;
		this.comment = comment;
		this.amount = 3;
		this.pics = new String[] { manag.photoFolderAddress + addr1, manag.photoFolderAddress + addr2, manag.photoFolderAddress + addr3 };
	}

	/*
	 * 6. Конструктор для записи массива фоток целиком (1 и более фоток), этот
	 * Конструктор помог избавитья от большого кол-ва конструкторов отличающихся
	 * только кол-ом фотографий. Т.е. теперь можо записывать в конструктор
	 * бесконечное кол-во фотографий одной игры. Сколько фоток есть в считываемом
	 * файле, столько и запишем
	 */

	public GameClass(AddressManager manag, String name, String creator, String mapper, String year, String comment, String[] pic) {
		
		this.name = name;
		this.creator = creator;
		this.mapper = mapper;
		this.year = year;
		this.comment = comment;

		if (pic.length == 1) { // если 1 фотка кастомная

			this.amount = 3;
			this.pics = new String[] { pic[0], manag.photoFolderAddress + addr2, manag.photoFolderAddress + addr3 };
		}

		else if (pic.length == 2) { // если 2 фотки кастомные

			this.amount = 3;
			this.pics = new String[] { pic[0], pic[1], manag.photoFolderAddress + addr3 };
		}

		else { // если 3 и более фотки кастомные

			this.amount = pic.length; // соответственно кол-во фоток определяется размером Массива
			this.pics = pic; // записываем в Поле pics массив адресов фоток целиком
		}
	}

///////////////////////////// Методы моего Класса (? штук) ///////////////////////////

	// 6 getters 

	// 1.1 getter of "name" field (метод по получению Данных поля "name")

	public String getName() {
		return name;
	}

	// 1.2 getter of "name" field (метод по получению Данных поля "name")

	public String getCreator() {
		return creator;
	}

	// 1.3 getter of "name" field (метод по получению Данных поля "name")

	public String getMapper() {
		return mapper;
	}

	// 1.4 getter of "year" field - используется только в MyComp (для сортировки по
	// году)

	public String getYear() {
		return year;
	}

	// 1.5 getter of "year" field - используется только в MyComp (для сортировки по
	// году)

	public String getComment() {
		return comment;
	}

	/* 1.6 получаем кол-во фоток данного объекта (игры) */
	
	public int getAmount () {
		
		return amount;
	}
	
	/* 2.1. Метод, который будет показывать фотки Объекта данного класса (т.е. Игры)
	 * Использую в методе public LocalListObject mouseChooseWindow() */
	
	public void showPics(AddressManager manag) {
		
		/* показываем Инфу по игре */
		
		gameInfo();
		
		/* фотки показываем, если есть адрес Папки с фотками в Менеджере адресов */
		
		if ( !manag.photoFolderAddress.equals("") ) {
			
			ImageIcon img;
		
			/* можно будет сделать так, что перед показом первой фотки, она по адресу
			 * проверялась на существование, если не сущ. - не показ. остальные */
			
			for (int i = 0; i < this.getAmount(); i++) {

				File check = new File (pics[0]);
				
				/* проверим адрес первой фотки на существование: да, смотрим дальше,
				 * нет - выходим из цикла */
				
				if (check.exists()) {
					
					img = new ImageIcon(pics[i]);
					JOptionPane.showMessageDialog(null, img, "That's the Game Photo", JOptionPane.PLAIN_MESSAGE);
					
				} else {
					
					ServiceMethods.windowShow("Something's wrong with pics of this Game");
					break;
				}
			}
		}
	}
	
	// 2.2. Метод по выводу в Окно (JOptionPane) информации об Игре (издатель, маппе и т.д.)

	private void gameInfo() {

		String text = "";
		String title;

		text = "  " + text + "\n" + "        Creator: " + this.getCreator() + "\n" + "        Maper:   "
				+ this.getMapper() + "\n" + "        Year:     " + this.getYear() + "\n" + "        Comment: "
				+ this.getComment() + "\n\n";

		// формируем надпись в заголовке Окна используя Статич. Переменные j и k

		title = this.getName();

		JTextArea textArea = new JTextArea(text);

		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setPreferredSize(new Dimension(400, 200));

		JOptionPane.showMessageDialog(null, scrollPane, title, JOptionPane.PLAIN_MESSAGE);

	} // конец Метода 2.2

} // конец Класса GameClass
