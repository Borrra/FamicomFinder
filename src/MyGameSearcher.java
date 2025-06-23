
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.swing.*;

public class MyGameSearcher {

	public static void main(String[] args) {

		/* заводим Менеджер адресов, а затем применяем его метод setting для формирования
		 * набора всех адресов нужных проекту и передаем его в методы */
		
		AddressManager manager = new AddressManager();

		manager = manager.setting(); // здесь сформирован окончательный Менеджер
		
		manager.showManagInfo(); // смотрим что содержит Менеждер
			
		/* создаем Объект нашего Списочного класса */
		
		/* здесь нужно будет решить какое первоначальное значение поля Inet_on_off будет */
		
		LocalListObject obj = new LocalListObject (manager);  // Конструктор №3

		while (!obj.key.equals("end")) {
			
			//manager.showManagInfo();
			
			/* 1. анализируем Ввод, заполняем поля объекта, составляем Списки */

			obj = obj.inputAnalyse(manager);

			/* 2. смотрим Информацию по объекту (это служебный метод) */

			obj.getInfo(manager);
			
			/* 3. выводим на экран список Игр, если же заполнен список Полей, выводим его */

			obj = obj.mouseChooseWindow(manager);
		}

		ServiceMethods.windowShow("конец программы");

	} // конец главного Метода main

} // конец Класса MyGameSearcher
