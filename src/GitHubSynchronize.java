import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.HashSet;
import java.util.Objects;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JTextField;

/* Здесь 3 класса: GitHubSynchronize, myWindow и consoleWindow, т.е.
 * в конце еще 2 класса по созданию Окон длительности процесса и Окон-Консолей 
 * 
 * В классе consoleWindow 2 метода, startWindow используется в Мет-ах 7 и 8
 * и toConsole в методах 5 и 6 */

/* Этот клас будет содержать методы по скачиванию моей Папки с фотографиями
 * Игр Проекта на компьютер, а также для сравнения фотографий на Git Hub и
 * на компе, и при различии, скачивать недостающие фотки с Git Hub на комп. */

public class GitHubSynchronize {

	/* у объекта будет 2 поля, имя папки и массив адресов загружаемых Фотографий */

	private String foldName;
	private String[] photoAddres;

	/* Единственный Конструктор Класса */

	GitHubSynchronize(String a, String[] b) {

		this.foldName = a;
		this.photoAddres = b;
	}

	/* 1. Метод получения Имени Папки */

	public String getName() {

		String a = this.foldName;

		return a;
	}

	/* 2. Метод получения Массива Адресов Фоток игр данной Папки */

	public String[] getPhotoAddres() {

		String[] a = this.photoAddres;

		return a;
	}

	/* 3. Метод получения одного адреса фотки игр данной Папки */

	public String getOnePhotoAddres(int b) {

		String a = this.photoAddres[b];

		return a;
	}

	/////////// Переопределим equals and hashCode Класса Object //////////////

	/*
	 * это для того, чтобы элементы моего класса, имели способность сравниватья друг
	 * с другом в Коллекциях, при применении метода (например) removeAll()
	 */

	@Override
	public boolean equals(Object obj) {

		// Check if the same reference

		if (this == obj)
			return true;

		// Check for null and type

		if (obj == null || getClass() != obj.getClass())
			return false;

		GitHubSynchronize myClass = (GitHubSynchronize) obj; // Cast to MyClass

		// Compare the name field

		if (!Objects.equals(foldName, myClass.foldName))
			return false;

		// Compare the values array
		return Arrays.equals(photoAddres, myClass.photoAddres);
	}

	@Override
	public int hashCode() {

		// Use Objects.hash for the name and Arrays.hashCode for the values array

		return Objects.hash(foldName) + Arrays.hashCode(photoAddres);
	}

/////////////////////////////////////////////////////////////////////////////////////////

	static long elapsedTime = 0; // для счета времени в Окне длительности процесса

	/*
	 * заведем Список Строк, куда будем писать строки считываемые из файла на
	 * Репозатории
	 */

	public static ArrayList<String> myList = new ArrayList<String>();

	/*
	 * 4. этот метод считывает инфу из моего текстового файла хранящегося на GitHub
	 * и заводит в моей папке текстовый файл (если его нету, а если есть,
	 * перезаписывать его) со всей скачанной инфой, т.е. он ничего не принимает и не
	 * возвращает, он Обновляет Текстовый Файл проекта (или создает его, если его
	 * вообще не было) Используется в main при вводе "refreshFile"
	 */

	public static void fileFromWebCreator(AddressManager manag) {

		// адрес моего файла на GitHub

		String fileUrl = manag.webFileAddress;

		try {

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

				// Читаем файл строка за строкой и добавляем строки в наш Список

				while ((line = in.readLine()) != null) {

					myList.add(line);
				}

				// Close the BufferedReader
				in.close();

			} else {

				System.out.println("Failed to fetch the file: " + connection.getResponseCode());
			}

			// Disconnect the connection
			connection.disconnect();

		} catch (IOException e) {

			e.printStackTrace();
		}

		/* Обработаем тут наш Список, для записи его в Файл */

		String[] myArray = myList.toArray(new String[0]); // переводим в Массив Строк

		// Service_Class.windowShow(myArray); // выводим в Окно

		File myTextFile = new File(manag.fileAddress); // объект типа File в папке Проекта
		// на рабочем столе (мой текст. файл)

		/*
		 * создаем Текстовый Файл на рабочем столе, если он еще не создан - случай когда
		 * файла изначально не было
		 */

		try { // первое try для первого создания файла

			/*
			 * в нашей Директории (рабочий стол) создаем наш файл. Только не совсем на
			 * рабочем. Он создается в папке MyGameSearcher на рабочем, даже если ее до
			 * этого не было - это над проверить
			 */

			if (myTextFile.createNewFile()) { // если файла не было, создаем

				System.out.println("Text file created good");

			} else { // если файл есть, перезаписываем

				// System.out.println ("Text file is already exists");
			}

		} catch (IOException e) { // конец первого try по созданию первого файла

			System.err.println("Error creating: " + e.getMessage());
		}

		/*
		 * если наш Текстовый файл существует, пишем туда инфу (а он должен
		 * существовать, т.к. в предыдущем участке кода мы его создавали)
		 */

		if (myTextFile.exists()) {

			try (BufferedWriter writer = new BufferedWriter(new FileWriter(manag.fileAddress, false))) {
				// try (PrintWriter writer = new PrintWriter(new FileWriter(fileAddres, false)))
				// {
				for (int i = 0; i < myArray.length; i++) {

					/* записываем в файл весь наш Массив строк, считанных с Репозатория */

					writer.write(myArray[i]);
					writer.newLine();
					// writer.write("\n"); // это вставил из-за PrintWriter
				}

				/*
				 * это "закрытие" я ввел по аналогии с закрытием потока при чтении инфы с инета.
				 * Я это ввел, и стал работать следующий блок - сейчас текстовый файл удаляется,
				 * но т.к. временного нет, он не переиминовывается
				 */

				writer.close(); // придумал сам

				// System.out.println ("file filled with info from the Internet");
				ServiceMethods.windowShow("Текст Файл обновлен.");

			} catch (IOException e) {

				e.printStackTrace();
			}

		}

	} // конец метода 4. fileFromWebCreator

	/* 5. мой метод для проверки, есть вообще Инет или нет 
	 * Перенес этот метод в Класс AddressManager */

	public static boolean isInternetAvailable(AddressManager manag) { // начало метода isInternetAvailable

		try {

			URL url = new URL(manag.inetChecking); // передаем адрес Интернет Ресурса
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			// urlConnection.setDefaultRequestProperty("User-Agent", "Test");
			// urlConnection.setDefaultRequestProperty("Connection", "close");
			urlConnection.setConnectTimeout(1000);
			urlConnection.connect();

			return (urlConnection.getResponseCode() == 200);

		} catch (Exception e) {

			return false;
		}

	} // конец метода 5. isInternetAvailable

	/*
	 * 6. Этот Метод обрабатывает Текстовый файл проекта лежащий на Git Hub и
	 * формирует из него список Объектов данного класса, т.е. список всех Папок
	 * фотографий проекта со списком файлов (фотографий) содержащихся в них. Т.е. по
	 * этому списку и будем создавать папки на компе и по этому же списку будем
	 * качать в них Фотографии
	 */

	public static GitHubSynchronize[] checkGitHubFolder(AddressManager manag) {

		Set<String> setList = new HashSet<>(); // сюда и будем собирать все адреса файлов

		GitHubSynchronize[] gitHubArr;

		/*
		 * заводим Список, в который будем записывать считанные объекты данного Класса
		 */

		List<GitHubSynchronize> folders = new ArrayList<>();

		try {

			// адрес моего Текстового файла на GitHub

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

					if (h > 5) { // если в папке есть фотки

						/*
						 * блок для выяснения (подсчета) есть ли в адресе фоток ссылка на папку General
						 * (чтобы не копировать лишние фотки). Если содержит, то k + 1 и в дальнейшем
						 * вычтем это число из кол-ва элементов массива адресов фоток одной игры
						 * ((h-5)-k1)
						 */

						int k1 = 0;

						for (int i = 5; i < parts.length; i++) {

							if (parts[i].substring(0, parts[i].indexOf('/')).equals("1. General")) {

								k1++;

								/*
								 * здесь будет формироваться массив строк названий фоток в папке General,
								 * специально в формате "1.jpg", "10.jpg" и др. чтобы мой Компаратор смог их
								 * отсортировать, а затем добавляем их в список set для дальнейшей обработки за
								 * пределами этого цикла
								 */

								String p = parts[i].substring(parts[i].indexOf('/') + 1);

								setList.add(p); // собираем уникальные названия фоток папки General в виде "3.jpg"
							}
						}

						/*
						 * дополнительно добавляю название фоток из папки General, которые не указаны в
						 * текстовом файле, а прописаны в конструкторе, и поэтому не могут быть скачены
						 * автоматически
						 */

						setList.add("1.jpg");
						setList.add("2.jpg");
						setList.add("3.jpg");

//						String name    = parts[0]; // это чтобы понимать почему счет идет с 5-го элемента
//						String creator = parts[1];
//						String mapper  = parts[2];
//						String year    = parts[3];
//						String comment = parts[4];

						/*
						 * создаем массив строк для Адресов фоток, с числом строк на столько меньше,
						 * сколько адресов ссылаются на папку General
						 */

						String[] pik = new String[(h - 5) - k1]; // создаем массив строк для Адресов фоток

						String FoldName = parts[5].substring(0, parts[5].indexOf('/')); // берем только название Папки

						/*
						 * заполняем Массив pik только Адресами фоток, считанных из файла, но если адрес
						 * ссылается на папку General, его не записываем
						 */

						for (int i = 5; i < parts.length; i++) {

							if (!(parts[i].substring(0, parts[i].indexOf('/')).equals("1. General"))) {

								pik[i - 5] = manag.webPhotoAddress + parts[i];
							}
						}

						/* создаем объект используя "универсальный" конструктор */

						GitHubSynchronize gameFolder = new GitHubSynchronize(FoldName, pik);

						folders.add(gameFolder); // добавляем объект в список folders

					} // конец if (h>5)

				} // конец основного цикла while

				// Close the BufferedReader
				in.close();

			} else {

				System.out.println("Failed to fetch the file: " + connection.getResponseCode());
			}

			// Disconnect the connection
			connection.disconnect();

		} catch (IOException e) {

			e.printStackTrace();
			System.out.println("Some shit is going on with the Internet");
		}

		///////////////////////////// блок обработки папки 1. General
		///////////////////////////// //////////////////////////////////////

		/*
		 * создаем один объект GitHubSynchronize, т.е под папку 1. General и добавим его
		 * в список папок на Git Hub
		 */

		String[] GeneralFolder = setList.toArray(new String[0]); // массив Адресов фоток из папки General в формате
																	// "1.jpg"

		Arrays.sort(GeneralFolder, new MyStringNameComp()); // сортируем массив строк (адресов) моим супер компаратором

		/*
		 * переводим адреса фоток в папке General из формата "1.jpg" в формат
		 * "https://raw.githubusercontent.com/ Borrra/GamesPhoto/main/1. General/1.jpg"
		 */

		for (int i = 0; i < GeneralFolder.length; i++) {

			GeneralFolder[i] = manag.webPhotoAddress + "1. General/" + GeneralFolder[i];
		}

		/*
		 * формируем Один объект класса GitHubSynchronize т.е. под папку General и
		 * добавляем его в наш Список
		 */

		GitHubSynchronize forGeneralFolder = new GitHubSynchronize("1. General", GeneralFolder);

		folders.add(forGeneralFolder); // добавляем объект 1. General в список GitHubList

		////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		gitHubArr = folders.toArray(new GitHubSynchronize[0]);

		/*
		 * вызываем Метод sort и передаем ему наш Компаратор MyWebNameComp(), он делает
		 * так, что наши элементы массива или Списка имели возможность сравниваться друг
		 * с другом, например в методе removeAll в дальнейшем
		 */

		Arrays.sort(gitHubArr, new MyWebNameComp());

		return gitHubArr;

	} // конец Метода № 6 по формированию Списка Папок на Git Hub

	/*
	 * 7. Метод который будет чекать локальную Папку с фотографиями Проекта и
	 * составлять список папок и фотографий в них, для того чтобы потом сравнить со
	 * списком содержащим Папки с Git Hub и полизвести обновление. Т.е. метод только
	 * составляет список содержимого Папки Проекта находящейся на Компьютере
	 * (включая Папку 1. General )
	 */

	public static GitHubSynchronize[] checkLocalFolder(AddressManager manag) {

		GitHubSynchronize[] compArr; // этот Массив будет возвращать Метод

		/*
		 * заводим Список, в который будем записывать считанные объекты данного Класса
		 */

		List<GitHubSynchronize> folders = new ArrayList<>();

		/*
		 * адрес Папки проекта (с фотками) завязываем на статической переменной
		 * GameClass.addres, удалив ее два последних символа
		 */

		// File projectFolder = new File(desktopPath + File.separator+ "MyGameSearcher"
		// + File.separator + "GamesPhoto");

		File projectFolder = new File(manag.fileAddress);

		/* проверим папку projectFolder на существование */

		if (projectFolder.exists()) {

			File[] localGameFolders = projectFolder.listFiles(File::isDirectory); // составляем список только папок в
																					// Папке фоток моего проекта

			String foldName; // строка под название Папки игры

			String[] addrPhoto; // массив строк под Адреса фоток Игры

			for (int i = 0; i < localGameFolders.length; i++) {

				foldName = localGameFolders[i].getName(); // берем название Папки игры

				/*
				 * записываем имена (названия фоток в папке) в Массив строк, сортируем его и
				 * добавляем путь к папке и ее название
				 */

				addrPhoto = localGameFolders[i].list(); // берем адреса фоток содержащихся в Папке игры (формат 1.jpg,
														// 2.jpg и др.)

				Arrays.sort(addrPhoto, new MyStringNameComp()); // наконец-то я отсортировал нормально адреса
																// фоток!!!!!!!

				for (int k = 0; k < addrPhoto.length; k++) {

					addrPhoto[k] = manag.webPhotoAddress + foldName + "/" + addrPhoto[k];
				}

				/* создаем объект используя "универсальный" конструктор */

				GitHubSynchronize gameFolder = new GitHubSynchronize(foldName, addrPhoto);

				folders.add(gameFolder); // добавляем объект в список folders
			}

			compArr = folders.toArray(new GitHubSynchronize[0]);

		} else {

			// downloadFullFolder (); // думаю здесь это не актуально

			/* если папка Проекта не существует, то метод вернет пустой массив (не null) */

			compArr = new GitHubSynchronize[0];
		}

		/* смотрим что за список получился */

		// GitHubSynchronize.windowShowFull (compArr, "Let's see local folders");

		return compArr;

	} // конец Метода № 7 checkLocalFolder()

	/*
	 * 8. Метод, который будет создавать Папку на Рабочем столе, аргументом
	 * передается Название файла
	 */

	public static String createFolder(AddressManager manag, String a) {

		// File folder = new File(GameClass.desktopPath, a);

		File folder;

		if (a.equals("")) { // по вводу "" создается папка Проекта на Рабочем Столе

			// ServiceMethods.windowShow ("First case");

			folder = new File(manag.desktopPath + File.separator + manag.projectFolderName);

		} else if (a.equals(" ")) { // по вооду " " создается папка GamesPhoto в папке проекта

			// ServiceMethods.windowShow ("Second case");

			folder = new File(manag.photoFolderAddress); // создаем папку с названием "а" в папке фотографий проекта

		} else { // по вооду чего-либо другово создается папка с таким названием в папке
					// GamesPhoto

			// ServiceMethods.windowShow ("Third case");

			folder = new File(manag.photoFolderAddress, a);
		}

		/*
		 * если папка не существует - создаем ее, иначе - она уже существует, в обоих
		 * случаях возвращаем Путь к этой папке
		 */

		if (!folder.exists()) {

			boolean folderCreated = folder.mkdir();

			if (folderCreated) { // если папка создалась - возвращаем Путь

				// System.out.println("\nFolder created: " + folder.getAbsolutePath() + "\n");

				consoleWindow.toConsole("\nFolder created: " + folder.getAbsolutePath() + "\n");

				return folder.getAbsolutePath();

			} else { // если не создалась - возвращаем null

				System.out.println("Failed to create folder.");

				return null;
			}

		} else {

			System.out.println("Folder already exists: " + folder.getAbsolutePath());

			return folder.getAbsolutePath();
		}

	} // конец Метода № 8

	/* 9. Метод который скачивает один файл с Git Hub по его адресу */

	public static String downloadOneFile(String fileUrl, String destinationFilePath) throws IOException {

		/*
		 * здесь нужно проверить существует ли папка с таким путем (destinationFilePath)
		 * или нет. если да, то качаем в нее файл, если нет -
		 */

		// System.out.println ("Downloaded file: " + destinationFilePath);
		// consoleWindow.toConsole (" " + destinationFilePath);

		URL url = new URL(fileUrl);

		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();

		httpConn.setRequestMethod("GET");

		// Check for HTTP response code

		int responseCode = httpConn.getResponseCode();

		if (responseCode == HttpURLConnection.HTTP_OK) {

			// Input stream to read the file

			InputStream inputStream = new BufferedInputStream(httpConn.getInputStream());

			/*
			 * для скачивания файла передаем его интернет адрес и адрес его расположения (не
			 * только адрес паки, но и название файла с рассширением, поэтому чтобы чекнуть
			 * папку, в которую мы будем качать файл, на существование, нужно последнюю
			 * часть с названием файла вырезать (3.jpg) и оставить
			 * C:\Users\Евгений\Desktop\MyGameSearcher\GamesPhoto\Dizzy
			 */

			String foldAdr = minusLastPart(destinationFilePath, "\\\\");

			// ServiceMethods.windowShow (foldAdr, "вот адрес папки для проверки - не
			// существует");

			File destFile = new File(foldAdr);

			if (destFile.exists()) {

				// System.out.println("Folder is exist");

				// String newFilePath = destinationFilePath;

				FileOutputStream outputStream = new FileOutputStream(destinationFilePath);
				// FileOutputStream outputStream = new FileOutputStream(destinationFilePath);

				byte[] buffer = new byte[4096];
				int bytesRead;

				// Read from input stream and write to output stream

				while ((bytesRead = inputStream.read(buffer)) != -1) {

					outputStream.write(buffer, 0, bytesRead);
				}

				outputStream.close();
				inputStream.close();

				httpConn.disconnect();

				/* в окно уже выводим адрес загруженного файла (фотки) */

				consoleWindow.toConsole(" " + destinationFilePath);

				return destinationFilePath;

			} else {

				// System.out.println("No folder to download file to");
				consoleWindow.toConsole("Что-то не так с адресом папки для скачивания файла");
				// consoleWindow.toConsole (foldAdr);

				httpConn.disconnect();

				return null;
			}

		} else {

			// System.out.println("No file to download. Server replied HTTP code: " +
			// httpConn.getResponseCode());
			consoleWindow.toConsole("No file to download. Server replied HTTP code: " + httpConn.getResponseCode());
			httpConn.disconnect();

			return null;
		}

		// httpConn.disconnect();

	} // конец Метода № 9 downloadOneFile

	/*
	 * 10. Метод который используется в main при работе с Компа, когда не нашлось
	 * папки, а Инет есть. Метод который обеспечивает скачивание фотографий с Git
	 * Hub из Папки проекта, он заводит папку за папкой и заполняет их
	 * соответствующими фотографиями. Метод будет качать файлы в любом случае, даже
	 * если они уже есть, он все равно будет пытаться из скачать, т.е. время на это
	 * все равно будет тратиться, значит этот метод целесообразно применять, когда
	 * папки вообще не существует (не нашлась)
	 */

	public static void downloadFullFolder(AddressManager manag) {

		/* заводим наш массив Папок/файлов на Git Hub */

		GitHubSynchronize[] arr = GitHubSynchronize.checkGitHubFolder(manag);

		/* здесь открываем мое Консоль-Окно для показа скачиаемых файлов */

		consoleWindow.startWindow("Скачиваем файлы:"); // мое Консоль-Окно

		////////////////////////// Блок открытия Окна длительности процесса в отдельном
		////////////////////////// Потоке ////////////////////

		SwingUtilities.invokeLater(() -> {

			myWindow.procesWindow("Внимание! Идет скачивание всех файлов проекта...");
		});

		////////////////////////////////////////////////////////////////////////////////////////////////////////

		/*
		 * потом нужно будте заменить эту переменную на GameClass.addres (универсальную)
		 */

		// String myAdr = "MyGameSearcher" + File.separator + "GamesPhoto"; // название
		// папки создаваемой в папке Проекта

		createFolder(manag, ""); // содаем папку Проекта (например C:\Users\Евгений\Desktop\MyGameSearcher )
		createFolder(manag, " "); // создаем папку GamesPhoto в папке Прокета

		/*
		 * каждую итерацию создаем папку в нашей корневой папке Проекта и в эту папку
		 * скачиваем фотки игр из такой папки на Git Hub
		 */

		for (int i = 0; i < arr.length; i++) { // этот цикл отвечает за 1 игру

			createFolder(manag, arr[i].getName()); // создаем папку Игры папке Games проекта

			/* этот цикл отвечает за скачивание всех фотографий одной игры в папку Игры */

			for (int k = 0; k < arr[i].photoAddres.length; k++) {

				try {

					/*
					 * Первый аргумент - это полный адрес фотки на Git Hub, причем пробелы в
					 * названии (если есть) нужно заменить на "%20", Второй аргумнт - это полный
					 * путь расположения файла, т.е. адрес созданой выше папки плюс название фотки
					 * (7.jpg например) напирмер: 1.
					 * https://raw.githubusercontent.com/Borrra/GamesPhoto/main/Dizzy/3.jpg 2.
					 * C:\Users\Евгений\Desktop\MyGameSearcher\GamesPhoto\Dizzy\3.jpg
					 */

					String phot = downloadOneFile(arr[i].photoAddres[k].replace(" ", "%20"), manag.photoFolderAddress
							+ arr[i].getName() + "\\" + getLastPartStartingWith(arr[i].photoAddres[k], '/'));

					/*
					 * если фотка не скачалась по какой-либо причине - конец проги (пока) потом
					 * придумаю что-нить
					 */

					if (phot == null) {

						return;
					}

					// System.out.println ("File downloaded successfully!");

				} catch (IOException e) {

					e.printStackTrace();
				}
			} // конец внутренного for
		} // конец внешнего for

		////////////////// Блок закрытия окна длительности процесса после того как поиск
		////////////////// закончился ////////////////////

		SwingUtilities.invokeLater(() -> {

			for (Frame frame : Frame.getFrames()) {

				frame.dispose();
			}

		});

/////////////////////////////////////////////////////////////////////////////////////////////////////////////

		ServiceMethods.windowShow("Это окно для создания задержки");

		/*
		 * окно оповещающее об окончании Процесса скачивания и укзании времени за
		 * которое это скачивание произошло
		 */

		ServiceMethods.windowShow("Скачивание завершено\nПроцесс занял " + elapsedTime + " секунд (ы) ("
				+ elapsedTime / 60 + " минут (ы)."); // для понимания того, что скачивание окончено

	} // конец Метода № 10 downloadFullFolder (GitHubSynchronize[] arr)

	/*
	 * 11. Метод который используется в main при вводе "refreshPhoto". Метод который
	 * чекает список Файлов на Git Hub, чекает папку на Компьютере, создает список
	 * разницы этих двух списков и скачивает недостающие файлы с Git Hub. Если фоток
	 * недостает в паке просто качаем их туда, если не хватает всех в папке, создаем
	 * ее и качаем фотки туда
	 */

	public static void differListPhotoToDownload(AddressManager manag) {

		Set<String> setList = new HashSet<>(); // сюда и будем собирать все адреса файлов

		///////// * формируем Список файлов на Git Hub *////////

		/* создаем Массив Папок на Git Hub используя наш Метод № 3 readWebFolders() */

		GitHubSynchronize[] gitHubArray = checkGitHubFolder(manag);

		/* заводим Список и добавляем в него предидущий Массив GitArray */

		Set<GitHubSynchronize> GitHubSet = new HashSet<GitHubSynchronize>();

		for (int i = 0; i < gitHubArray.length; i++) {

			GitHubSet.add(gitHubArray[i]);
		}

		// String myAdr = "MyGameSearcher" + File.separator + "GamesPhoto"; // название
		// папки создаваемой в папке Проекта

		windowShowFull(gitHubArray, "Это файлы на Git Hub"); // Первый показ - Список на Git Hub

		///////////////// * формируем Список файлов на Компе *//////////////////////////

		GitHubSynchronize[] compArray = checkLocalFolder(manag);

		if (compArray.length == 0) {

			ServiceMethods.windowShow("Файлов на компе нет");

			return;
		}

		Set<GitHubSynchronize> compSet = new HashSet<GitHubSynchronize>();

		for (int i = 0; i < compArray.length; i++) {

			compSet.add(compArray[i]);
		}

		windowShowFull(compArray, "Это файлы на Компе"); // Второй показ - Список на Компе

		/*
		 * применяем метод removeAll чтобы удалить из списка на Git Hub элементы,
		 * которые есть на компьютере
		 */

		GitHubSet.removeAll(compSet);

		/*
		 * в итоге мы получили список элементов нашего Списка, список адресов фотографий
		 * которых отличается от списка адресов фоток на компе. Т.е. это еще не
		 * окончательный список фотографий, потому что это элементы целиком, а нам
		 * интересны отдельные фотографии
		 */

		/* получем "разничный" Массив */

		GitHubSynchronize[] difArray = GitHubSet.toArray(new GitHubSynchronize[0]); // массив, который возвращает Метод

		/*
		 * код, который удаляет все файлы из папкок с расхождениями на Компе, перед
		 * закачкой в них новых
		 */

		/////////////////////////////////////////////////////////////////////////////////////////////////////////

		// consoleWindow.startWindow("Удаляем файлы: ");

		for (int i = 0; i < difArray.length; i++) {

			/*
			 * вот как раз "GameClass.addres + difArray[i].getName()" и будет адрес папки с
			 * фотками, которую нужно очистить
			 */

			// ServiceMethods.windowShow(GameClass.addres + difArray[i].getName(), "адреса
			// папок?");

			/* удаляем из папки все файлы */

			deleteFiles(manag.photoFolderAddress + difArray[i].getName());
		}

		// consoleWindow.toConsole("end");

		/////////////////////////////////////////////////////////////////////////////////////////////////////////

		Arrays.sort(difArray, new MyWebNameComp()); // сортируем его

		/*
		 * далее идет блок который работает, только есл и массиве difArray есть
		 * элементы, т.е если есть разница в файлай на Компе и на Git Hub, т.е если есть
		 * что скачивать
		 */

		if (difArray.length > 0) {

			windowShowNames(difArray, "Это папки с расхождениями"); // показываем наш "разничный" массив на Git Hub
																	// (массив Объектов)

			/*
			 * мы получили массив Объектов в адресах которых есть расхождения с файлами на
			 * Компе. Т.е. это еще не конкретные адреса фоток. Вот дальше и будем вычислять
			 * адреса конкретных фоток, которые нужно скачивать
			 */

			/*
			 * этот метод открывает мое Консоль-Окно, где будет показываться скачиваемыме
			 * файлы (онлайн) оно закроется с окончанием действия данного метода -
			 * differListPhotoToDownload ()
			 */

			consoleWindow.startWindow("Скачиваем файлы:"); // мое Консоль-Окно

			////////////////////////// Блок открытия Окна длительности процесса в отдельном
			////////////////////////// Потоке ////////////////////

			SwingUtilities.invokeLater(() -> {

				myWindow.procesWindow("Внимание! Идет скачивание...");
			});

			/////////////////////////////////////////////////////////////////////////////////////////////////////////

			for (int t = 0; t < difArray.length; t++) { // цикл длиной равной "разничному" массиву (несколько игр
														// допустим)

				int w = 0; // для понимания есть ли вообще папка на компе, в которой расхождение или надо
							// качать все ее файлы

				for (int r = 0; r < compArray.length; r++) { // цикл длиной равной всему массиву Папок на компе

					/*
					 * условие когда Имя Папки в которой найдено расхождение совпадает с Именем
					 * Папки на Git Hub т.е. она есть на компе, но в ней не хватает файлов (надо
					 * проверить будет, что если там будут лишнее файлы) с которой и будем качать
					 * недостающие элементы (фотки)
					 */

					/*
					 * по мере опробования изменил логику. если в папке найдено (хоть одно)
					 * несовпадение, качаются все фотки этой папки с Git Hub, и заменяют
					 * существующие. А в будущем надо сделать так, чтобы сначала все форки из этой
					 * папки удалялись
					 */

					if (compArray[r].getName().equals(difArray[t].getName())) {

						/*
						 * теперь списки адресов фоток на Git Hub и на Компе в папках с расхождениями
						 * добавим в "уникальные" списки set1 и set2 для сравнения
						 */

						// Set <String> set1 = new HashSet<>(
						// Arrays.asList(compArray[r].getPhotoAddres() )); // фотки Mortal Kombat на
						// компе
						Set<String> set2 = new HashSet<>(Arrays.asList(difArray[t].getPhotoAddres())); // фотки Mortal
																										// Kombat на Git
																										// Hub

						/*
						 * если сейас мы применим set2.removeAll(set1), то в списке set2 остануться
						 * адреса фотографий на Git Hub, которых нет на Компе и следовательно которые
						 * нужно скачивать!
						 */

						// set2.removeAll(set1);

						setList.addAll(set2);

						/*
						 * в этом блоке нужно сделать так, чтобы список set2 (а далее Массив) скачавался
						 * в соответствующую папку
						 */

						/* этот цикл отвечает за скачивание всех фотографий одной игры в папку Игры */

						String[] foldArray = set2.toArray(new String[0]);

						for (int k = 0; k < foldArray.length; k++) { // цикл для скачавания фоток в существующую папку

							try {

								/*
								 * Первый аргумент - это полный адрес фотки на Git Hub, причем пробелы в
								 * названии (если есть) нужно заменить на "%20", Второй аргумнт - это полный
								 * путь расположения файла, т.е. адрес созданой выше папки плюс название фотки
								 * (7.jpg например) напирмер: 1.
								 * https://raw.githubusercontent.com/Borrra/GamesPhoto/main/Dizzy/3.jpg 2.
								 * C:\Users\Евгений\Desktop\MyGameSearcher\GamesPhoto\Dizzy\3.jpg
								 */

								downloadOneFile(foldArray[k].replace(" ", "%20"),
										manag.photoFolderAddress + getPreLastPartStartingWith(foldArray[k], '/') + "\\"
												+ getLastPartStartingWith(foldArray[k], '/'));

								// System.out.println ("File downloaded successfully!");

							} catch (IOException e) {

								e.printStackTrace();
								System.out.println("Some shit is going on with the Internet");
							}
						} // конец супер внутренного for

						w = 1; // означает, что Папка с расхождением есть на компе
					}

				} // конец внутренего цикла for

				/*
				 * если после окончания внешней итерации w==0, значит Папка с расхождением
				 * отсутствует на Компе, а значит все ее фотки надо скачивать (т.е. добавляем их
				 * все в список)
				 */

				if (w == 0) {

					Set<String> set3 = new HashSet<>(Arrays.asList(difArray[t].getPhotoAddres()));

					setList.addAll(set3);

					/* здесь организуем создание недостающей Папки и скачивание туда всех фоток */

					// createFolder (myAdr + File.separator + difArray[t].getName() ); // создаем
					// папку Игры
					createFolder(manag, difArray[t].getName()); // создаем папку Игры

					/* скачаваем в нее файлы */

					for (int k = 0; k < difArray[t].getPhotoAddres().length; k++) { // цикл скачивающий фотки в только
																					// что созданную папку

						try {

							/*
							 * Первый аргумент - это полный адрес фотки на Git Hub, причем пробелы в
							 * названии (если есть) нужно заменить на "%20", Второй аргумнт - это полный
							 * путь расположения файла, т.е. адрес созданой выше папки плюс название фотки
							 * (7.jpg например) напирмер: 1.
							 * https://raw.githubusercontent.com/Borrra/GamesPhoto/main/Dizzy/3.jpg 2.
							 * C:\Users\Евгений\Desktop\MyGameSearcher\GamesPhoto\Dizzy\3.jpg
							 */

							// downloadOneFile (difArray[t].getPhotoAddres()[k].replace(" ", "%20"),
							// GameClass.desktopPath + "\\" + myAdr + File.separator
							downloadOneFile(difArray[t].getPhotoAddres()[k].replace(" ", "%20"),
									manag.photoFolderAddress + getPreLastPartStartingWith(difArray[t].getPhotoAddres()[k], '/')
											+ "\\" + getLastPartStartingWith(difArray[t].getPhotoAddres()[k], '/'));

							// System.out.println ("File downloaded successfully!");

						} catch (IOException e) {

							e.printStackTrace();
							System.out.println("Some shit is going on with the Internet");
						}

					} // конец внутренного for

				} // конец if, когда папка полностью отсутствовала

			} // конец внешнего цикла for

			// consoleWindow.toConsole("end"); // почему-то заканчивается вся программа

			/////////////////// Блок закрытия окна длительности процесса после того как
			/////////////////// поиск закончился ////////////////////

			SwingUtilities.invokeLater(() -> {

				for (Frame frame : Frame.getFrames()) {

					frame.dispose();
				}

			});

			//////////////////////////////////////////////////////////////////////////////////////////////////////////////

		} else { // конец if (difArray > 0)

			ServiceMethods.windowShow("Полное совпадение!");
		}

		/*
		 * если в списке что-то есть, делаем из него Массив и выводим на экран, если
		 * нет, пишем, что совпадение Полное
		 */

		if (setList.size() > 0) {

			String[] filesToDownloadArray = setList.toArray(new String[0]);

			Arrays.sort(filesToDownloadArray, new MyStringNameComp());

			ServiceMethods.windowShow("Временное Окно для задержки"); // почему-то это окно быстро закрывается, а
																		// следующее нормально

			ServiceMethods.windowShow(filesToDownloadArray, "Скаченные файлы: ");

		} // else {

		// ServiceMethods.windowShow ("Полное совпадение!");
		// }

	} // конец Метода № 11 differListPhotoToDownload ()

	/*
	 * 12. Метод который чекает список Файлов на Git Hub, чекает папку на
	 * Компьютере, создает список разницы этих двух списков и составляет список
	 * файлов, которые нужно загружать
	 */

	public static void fileListToDownload(AddressManager manag) {

		///////// * формируем Список файлов на Git Hub *////////

		Set<String> setList = new HashSet<>(); // сюда и будем собирать все адреса файлов

		/* создаем Массив Папок на Git Hub используя наш Метод № 3 readWebFolders() */

		GitHubSynchronize[] gitHubArray = checkGitHubFolder(manag);

		/* заводим Список и добавляем в него предидущий Массив GitArray */

		Set<GitHubSynchronize> GitHubSet = new HashSet<GitHubSynchronize>();

		for (int i = 0; i < gitHubArray.length; i++) {

			GitHubSet.add(gitHubArray[i]);
		}

		// windowShowFull(gitHubArray, "Это файлы на Git Hub"); // Первый показ - Список
		// на Git Hub

		///////////////// * формируем Список файлов на Компе *//////////////////////////

		GitHubSynchronize[] compArray = checkLocalFolder(manag);

		Set<GitHubSynchronize> compSet = new HashSet<GitHubSynchronize>();

		for (int i = 0; i < compArray.length; i++) {

			compSet.add(compArray[i]);
		}

		// windowShowFull(compArray, "Это файлы на Компе"); // Второй показ - Список на
		// Компе

		/*
		 * применяем метод removeAll чтобы удалить из списка на Git Hub элементы,
		 * которые есть на компьютере
		 */

		GitHubSet.removeAll(compSet);

		/*
		 * в итоге мы получили список элементов нашего Списка, список адресов фотографий
		 * которых отличается от списка адресов фоток на компе. Т.е. это еще не
		 * окончательный список фотографий, потому что это элементы целиком, а нам
		 * интересны отдельные фотографии
		 */

		/* получем "разничный" Массив */

		GitHubSynchronize[] difArray = GitHubSet.toArray(new GitHubSynchronize[0]); // массив, который возвращает Метод

		Arrays.sort(difArray, new MyWebNameComp()); // сортируем его

		if (difArray.length > 0) {

			windowShowNames(difArray, "Это папки с расхождениями"); // показываем наш "разничный" массив на Git Hub
																	// (массив Объектов)
		} else {
			ServiceMethods.windowShow("Нет файлов для загрузки.");
			return;
		}

		/*
		 * мы получили массив Объектов в адресах которых есть расхождения с файлами на
		 * Компе. Т.е. это еще не конкретные адреса фоток. Вот дальше и будем вычислять
		 * адреса конкретных фоток, которые нужно скачивать
		 */

		for (int t = 0; t < difArray.length; t++) { // цикл длиной равной "разничному" массиву (несколько игр допустим)

			int w = 0; // для понимания есть ли вообще папка на компе, в которой расхождение или надо
						// качать все ее файлы

			for (int r = 0; r < compArray.length; r++) { // цикл длиной равной всему массиву Папок на компе

				/*
				 * условие когда Имя Папки в которой найдено расхождение совпадает с Именем
				 * Папки на Git Hub т.е. она есть на компе, но в ней не хватает файлов (надо
				 * проверить будет, что если там будут лишнее файлы) с которой и будем качать
				 * недостающие элементы (фотки)
				 */

				if (compArray[r].getName().equals(difArray[t].getName())) {

					/*
					 * теперь списки адресов фоток на Git Hub и на Компе в папках с расхождениями
					 * добавим в "уникальные" списки set1 и set2 для сравнения
					 */

					Set<String> set1 = new HashSet<>(Arrays.asList(compArray[r].getPhotoAddres())); // фотки Mortal
																									// Kombat на компе
					Set<String> set2 = new HashSet<>(Arrays.asList(difArray[t].getPhotoAddres())); // фотки Mortal
																									// Kombat на Git Hub

					/*
					 * если сейас мы применим set2.removeAll(set1), то в списке set2 остануться
					 * адреса фотографий на Git Hub, которых нет на Компе и следовательно которые
					 * нужно скачивать!
					 */

					set2.removeAll(set1);

					setList.addAll(set2);

					w = 1; // означает, что Папка с расхождением есть на компе
				}

			} // конец внутренего цикла for

			/*
			 * если после окончания внешней итерации w==0, значит Папка с расхождением
			 * отсутствует на Компе, а значит все ее фотки надо скачивать (т.е. добавляем их
			 * все в список)
			 */

			if (w == 0) {

				Set<String> set3 = new HashSet<>(Arrays.asList(difArray[t].getPhotoAddres()));

				setList.addAll(set3);

			} // конец if, когда папка полностью отсутствовала

		} // конец внешнего цикла for

		/*
		 * если в списке что-то есть, делаем из него Массив и выводим на экран, если
		 * нет, пишем, что совпадение Полное
		 */

		String[] filesToDownloadArray = setList.toArray(new String[0]);

		Arrays.sort(filesToDownloadArray, new MyStringNameComp()); // сортируем его

		if (filesToDownloadArray.length > 0) {

			windowShowStringArray(filesToDownloadArray, "Отсутствующие файлы: ");

		} else {

			ServiceMethods.windowShow("Полное совпадение! Нет файлов для загрузки.");
		}

	} // конец Метода № 12 fileListToDownload ()

	/* 13. Метод по удалению всех файлов из Папки */

	public static void deleteFiles(String directoryPath) {

		// consoleWindow.startWindow("Удаляем файлы: ");

		// Specify the directory path

		// String directoryPath = "path/to/your/directory";

		// Create a File object for the directory

		File directory = new File(directoryPath);

		// Check if the directory exists and is indeed a directory

		if (directory.exists() && directory.isDirectory()) {

			// List all files in the directory

			File[] files = directory.listFiles();

			if (files != null) {

				// Iterate through each file and delete it

				for (File file : files) {

					if (file.isFile()) {

						boolean deleted = file.delete();

						if (deleted) {

							// consoleWindow.toConsole("Deleted file: " + file.getAbsolutePath());
							// System.out.println("Deleted file: " + file.getAbsolutePath());

						} else {

							System.out.println("Failed to delete file: " + file.getName());
						}
					}
				}

			} else {

				System.out.println("The directory is empty or an I/O error occurred.");
			}

		} else {

			// System.out.println("The specified path is not a directory or does not
			// exist.");
		}

	} // конец 13. метода deleteFiles

	//////////////////// * Далее идет 5 служебный класса
	//////////////////// *////////////////////////////

	/*
	 * 1 Метод служебный, для вырезания названия фотки (1.jpg) из строки Адреса для
	 * записи ее в папку: было
	 * "C:\Users\Евгений\Desktop\MyGameSearcher\GamesPhoto\Dizzy\3.jpg" стало
	 * "3.jpg"
	 */

	public static String getLastPartStartingWith(String input, char delimiter) {

		// Split the string by the delimiter

		String[] parts = input.split(String.valueOf(delimiter));

		// Check if we have any parts and return the last one

		if (parts.length > 0) {

			return parts[parts.length - 1]; // Return the last part
		}

		return null;

	} // коней Метода № 1

	/*
	 * 2 Метод служебный, для вырезания названия папки (Mortal Kombat) для записи ее
	 * в папку
	 */

	public static String getPreLastPartStartingWith(String input, char delimiter) {

		// Split the string by the delimiter

		String[] parts = input.split(String.valueOf(delimiter));

		// Check if we have any parts and return the last one

		if (parts.length > 0) {

			return parts[parts.length - 2]; // Return the last part
		}

		return null;

	} // коней Метода № 2

	/*
	 * 3. Метод разбивает передаваемую ему строку на строки используя в качестве
	 * метки строку, передаваемую вторым аргументом и возвращает строку (первй
	 * аргумент) за исключением последней части: было
	 * "C:\Users\Евгений\Desktop\MyGameSearcher\GamesPhoto\Dizzy\3.jpg" - адрес
	 * файла стало: "C:\Users\Евгений\Desktop\MyGameSearcher\GamesPhoto\Dizzy" -
	 * адрес папки
	 */

	public static String minusLastPart(String input, String delimiter) {

		// Split the string by the delimiter

		String[] parts = input.split(String.valueOf(delimiter));

		String ka;

		// Check if we have any parts and return the last one

		if (parts.length > 0) {

			// ServiceMethods.windowShow(parts[parts.length - 1], "эту часть удаляем"); //
			// смотрим последнюю часть

			ka = input.substring(0, input.indexOf(parts[parts.length - 1]));

			// ServiceMethods.windowShow(ka, "этот адрес получается"); // смотрим что
			// получилось

			return ka; // возвращаем встроку без последней части
		}

		return null;

	} // коней Метода № 3

	/* 4 Метод выводит Массив Строк в Окно + заголовок Окна */

	public static void windowShowStringArray(String[] arrg, String k) {

		String text = "";
		String title;

		for (int i = 0; i < arrg.length; i++) {

			String ch = String.format("%03d", i + 1);
			text = text + ch + ". " + arrg[i] + "\n";
		}

		// формируем надпись в заголовке Окна используя Статич. Переменные j и k

		title = "There're " + arrg.length + " element (s). " + k;

		JTextArea textArea = new JTextArea(text);
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setPreferredSize(new Dimension(620, 500));

		JOptionPane.showMessageDialog(null, scrollPane, title, JOptionPane.PLAIN_MESSAGE);
	}

	/* 5 Метод по выводу в Окно списка всех Папок и адресов Фоток в них */

	public static void windowShowFull(GitHubSynchronize[] arrg, String p) {

		String text = "";
		String title;

		for (int i = 0; i < arrg.length; i++) {

			String ch = String.format("%03d", i + 1);
			text = text + ch + ". " + arrg[i].getName() + "\n";

			for (int k = 0; k < arrg[i].getPhotoAddres().length; k++) {

				text = text + arrg[i].getPhotoAddres()[k] + "\n";
			}

			text = text + "\n";
		}

		// формируем надпись в заголовке Окна используя Статич. Переменные j и k

		title = "There're " + arrg.length + " elements. " + p + " .";

		JTextArea textArea = new JTextArea(text);
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setPreferredSize(new Dimension(500, 500));

		JOptionPane.showMessageDialog(null, scrollPane, title, JOptionPane.PLAIN_MESSAGE);

	} // конец Метода № 5

	/* 6 Метод по выводу в Окно списка всех Папок и адресов Фоток в них */

	public static void windowShowNames(GitHubSynchronize[] arrg, String p) {

		String text = "";
		String title;

		for (int i = 0; i < arrg.length; i++) {

			String ch = String.format("%03d", i + 1);
			text = text + ch + ". " + arrg[i].getName() + "\n";
		}

		// формируем надпись в заголовке Окна используя Статич. Переменные j и k

		title = "There're " + arrg.length + " elements. " + p + " .";

		JTextArea textArea = new JTextArea(text);
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setPreferredSize(new Dimension(500, 500));

		JOptionPane.showMessageDialog(null, scrollPane, title, JOptionPane.PLAIN_MESSAGE);

	} // конец Метода № 6

} // конец Класса GitHubSynchronize (с десятью методами)

/*
 * этот Класс myWindow формирует JPanel с возможностью нажатия клавишь, а Метод
 * в конце применяет эту JPanel в JOptionPane.showMessageDialog(null, myPanel,
 * a, JOptionPane.PLAIN_MESSAGE), и этот же метод (procesWindow) применяется в
 * Методе 2.6 в Service_Class, в окне длительности процесса
 */

class myWindow extends JPanel implements ActionListener {

	/*
	 * т.е. мы как бы делаем свою Кастомную JPanel, которя применяет интерфейс
	 * ActionListener, для возможности использования кнопок
	 */

/////////* устанавливаем поля нашего Класса SnakeGame *//////////////

	private final int CELL = 20;
	private final int WIDTH = 310; // ширина окна
	private final int HEIGHT = 30; // высота окна

	private Timer timer;
	private long startTime;

	/* заводим Связанный Список для координат каждого Кубика Змеи */

	private LinkedList<Point> stuff = new LinkedList<Point>();

	public myWindow(String k) { // Конструктор Класса

		stuff.add(new Point(0, 7)); // начальное положение Змеи
		stuff.add(new Point(WIDTH - 180, 7));
		stuff.add(new Point(WIDTH - 180 + CELL, 7));

		/*
		 * задаем Размеры Окна, причем высоту задаем с учетом размера нижней,
		 * дополнительной зоны отображения Очков SCORE_HIGHT
		 */

		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setBackground(Color.BLACK);
		setFocusable(true);

		/* регистрируем Приемник */

		addKeyListener(new KeyAdapter() {

			/* Класс KeyEvent */

			@Override
			public void keyPressed(KeyEvent e) {

				/*
				 * метод getKeyCode() возвращает код нажатой кнопки (просто число),
				 * соответственно по этому коду уже срабатывают Кейсы - их может быть сколько
				 * угодно
				 */

				switch (e.getKeyCode()) {

				/*
				 * если менять координаты точки в этом блоке, то она будет двигаться только по
				 * мере нажатия клавиши, поэтому при нажатии стрелок меняется только значение
				 * token, а уже в Методе Snake будет действия на этот token
				 */

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

////////////////* переориентированные Методы класа JPanel *///////////////////

	@Override
	protected void paintComponent(Graphics g) { // то, что будет отображаться в Окне
		super.paintComponent(g);

		g.setColor(Color.RED);

		for (int i = 0; i < stuff.size(); i++) {

			g.fillRect(stuff.get(i).x, stuff.get(i).y, CELL, CELL);
		}

		long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;

		g.drawString("Seconds: " + elapsedTime, WIDTH - 180, HEIGHT - 10);

		GitHubSynchronize.elapsedTime = elapsedTime;
	}

	@Override
	public void actionPerformed(ActionEvent e) { // метод который и делает Экшен

		moveSnake();

		repaint();
	}

	public void moveSnake() { // метод который будет как бы циклиться в actionPerformed

		stuff.addFirst(new Point(stuff.getFirst().x + CELL, stuff.getFirst().y));

		stuff.removeLast(); // убираем Хвост

		if (stuff.getFirst().x >= WIDTH) {
			stuff.getFirst().x = 0;
		}
	}

	/*
	 * вот этот метод мы и используем в Service_Class в методе 2.6 для отображения
	 * длительности процесса
	 */

	public static void procesWindow(String a) {

		/* активируем наше Кастомное Окно, оно является JPanel по сути */

		myWindow myPanel = new myWindow("stuff");

		JOptionPane.showMessageDialog(null, myPanel, a, JOptionPane.PLAIN_MESSAGE);

	}

} // конец Класса myWindow

class consoleWindow {

	private static JTextArea textArea; // Area for displaying console output
	private static JTextField inputField; // Field for user input

	/*
	 * мое Консоль-Окно создается в Методе. Поэтому видимо оно и закрывается само,
	 * конда метод заканчивает свою работу
	 */

	/* этот метод используется в Методах основного класса № 7 и 8 */

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

	} // конец Метода wind

	// Method to append text to the console area

	/* этот метод используется в Методах основного класса № 5 и 6 */

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

	} // конец метода toConsole

} // конец Класса consoleWindow