 
public class MyGameSearcher {

	public static void main(String[] args) {

		/* создаем Менеджер адресов пока только с web адресами */
		
		AddressManager manager = new AddressManager();

		/* создаем Объект нашего Списочного класса */
		
		GameListClass obj = new GameListClass (manager);

		/* глвыный цикл программы */
		
		while (!obj.key.equals("end") && !obj.key.equals("конец") ) {

			/* 1. анализируем Ввод, составляем Списки */

			obj = obj.inputAnalyse(manager);

			/* условие обновления Менеджера */
			
			if ( obj.key.equals("refreshManag") ) {
				
				manager = manager.setting();

				/* на основе обновленого Менеждера обновляем объект */
				
				obj = new GameListClass (manager);
			}

			/* 2. выводим на экран список Игр, если же заполнен
			 * список Полей, выводим его */

			obj = obj.mouseChooseWindow();
			
		} // конец главного цикла

		ServiceMethods.windowShow("конец программы");

	} // конец главного Метода main

} // конец Класса MyGameSearcher
