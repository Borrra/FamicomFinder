 
public class MyGameSearcher {

	public static void main(String[] args) {

		/* заводим Менеджер адресов, а затем применяем его метод setting для формирования
		 * набора всех адресов нужных проекту и передаем его в методы */
		
		AddressManager manager = new AddressManager();

		manager = manager.setting(); // здесь сформирован окончательный Менеджер

		/* создаем Объект нашего Списочного класса */
		
		GameListClass obj = new GameListClass (manager);  // Конструктор №1

		while (!obj.key.equals("end") && !obj.key.equals("конец") ) {

			/* 1. анализируем Ввод, заполняем поля объекта, составляем Списки */

			obj = obj.inputAnalyse(manager);

			/* условие "принудительного" обновления Менеджера */
			
			if ( obj.key.equals("refreshManag") ) {
				
				manager = manager.setting();
				
				continue;
			}
			
			/* 2. выводим на экран список Игр, если же заполнен список Полей, выводим его */

			obj = obj.mouseChooseWindow(manager);
		}

		ServiceMethods.windowShow("конец программы"); // чтоб видеть, что прога закончилась

	} // конец главного Метода main

} // конец Класса MyGameSearcher
