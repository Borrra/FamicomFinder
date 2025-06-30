
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.swing.*;

public class MyGameSearcher {

	public static void main(String[] args) {

		//////////// эксперимент/////////////
		
		GitSiteSynchronize synch = new GitSiteSynchronize ();
	
		/////////////////////////////////////
		
		/* заводим Менеджер адресов, а затем применяем его метод setting для формирования
		 * набора всех адресов нужных проекту и передаем его в методы */
		
		AddressManager manager = new AddressManager();

		manager = manager.setting(); // здесь сформирован окончательный Менеджер

		////////////эксперимент/////////////////
		
		synch.downloadDiffArray(manager);
		
		////////////////////////////////////////
		
		/* создаем Объект нашего Списочного класса */
		
		GameListClass obj = new GameListClass (manager);  // Конструктор №1

		while (!obj.key.equals("end") && !obj.key.equals("конец") ) {

			/* 1. анализируем Ввод, заполняем поля объекта, составляем Списки */

			obj = obj.inputAnalyse(manager);

			/* 2. выводим на экран список Игр, если же заполнен список Полей, выводим его */

			obj = obj.mouseChooseWindow(manager);
		}

		ServiceMethods.windowShow("конец программы"); // чтоб видеть, что прога закончилась

	} // конец главного Метода main

} // конец Класса MyGameSearcher
