import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/* Класс с одним Методом, который организует Список Объектов, который он считывает из Репозитория на GitHub.
 * Т.е. считывает информацию из файла на GitHub и формирует из него Список Объектов (Игр). Применяется в
 * главном методе в Классе MyGameSearcher, при формировании Массива Объектов */

public class GitHubListObject {

	public static List<GameClass> readGamesFromWeb(AddressManager manag) {

		List<GameClass> games = new ArrayList<>();

		try {

			//String fileUrl = GameClass.webFileAddres; // веб адрес моего текст файла на Git Hub

			String fileUrl = manag.webFileAddress;
			
			// Создаем URL объект используя мой адрес

			URL url = new URL(fileUrl);

			// Открываем соединение к URL

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setRequestMethod("GET"); // Set the request method to GET

			// Check if the response code is HTTP_OK (200)

			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

				// Create a BufferedReader to read the input stream
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

				String line;
				String result = "";
				int count = 0;

				// Читаем файл строка за строкой и добавляем строки в наш Список

				while ((line = in.readLine()) != null) {

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
					 * полей (переменная h), выбирается соответствующий Конструктор, из всего 11
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

					if (h == 1) {

						String name = parts[0];

						GameClass person = new GameClass(manag, name);
						games.add(person);
					}

					else if (h == 2) {

						String name = parts[0];
						String creator = parts[1];

						GameClass person = new GameClass(manag, name, creator);
						games.add(person);
					}

					else if (h == 3) {

						String name = parts[0];
						String creator = parts[1];
						String year = parts[2];

						GameClass person = new GameClass(manag, name, creator, year);
						games.add(person);
					}

					else if (h == 4) {

						String name = parts[0];
						String creator = parts[1];
						String mapper = parts[2];
						String year = parts[3];

						GameClass person = new GameClass(manag, name, creator, mapper, year);
						games.add(person);
					}

					else if (h == 5) {

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

				} // конец while

				// Close the BufferedReader
				in.close();

			} else {

				System.out.println("Failed to fetch the file: " + connection.getResponseCode());
			}

			// Disconnect the connection
			connection.disconnect();

		} catch (IOException e) {

			// System.out.println("Fuck!");
			e.printStackTrace();
		}

		// вызываем Метод sort и передаем ему наш Компаратор, это сортирует нашу
		// Коллекцию (либо по Алфавиту либо по Году выпуска)

		games.sort(new MyNameCompNew());
		// games.sort(new MyYearComp());

		return games;

	} // конец единственного Метода readGamesFromWeb

} // конец Класса
