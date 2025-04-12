import java.util.Scanner;
public final class Storyteller{

  private static Scanner sc = new Scanner(System.in);

  public static void read(String line){
    System.out.print(line);
    sc.nextLine();
  }

  public static String readAsk(String line){
    System.out.print(line);
    return sc.nextLine();
  }

  public static String ask(String question, String[] options){
    if (!question.equals("")){
      System.out.println(question);
    }
    for (String i: options){
      System.out.println(i);
    }
    String answer = new String(sc.nextLine()).toLowerCase();
    for (String j: options){
      if (answer.equals(j.toLowerCase())){
        return answer;
      }
    }
    System.out.println("That is not an available option.");
    return ask(question, options);
  }

  public static String ask(String question, String[] options, String[] valid, String incorrect){
    if (!question.equals("")){
      System.out.println(question);
    }
    for (String i: options){
      System.out.println(i);
    }
    String answer = new String(sc.nextLine());
    for (String j: valid){
      if (answer.toUpperCase().equals(j.toUpperCase())){
        return answer;
      }
    }
    System.out.println(incorrect);
    return ask(question, options, valid, incorrect);
  }

  public static int ask(String question, String[] options, int[] valid, String incorrect){
    if (!question.equals("")){
      System.out.println(question);
    }
    for (String i: options){
      System.out.println(i);
    }
    String input = new String(sc.next());
    int answer = 0;
    try {
      answer = Integer.parseInt(input);
    } 
    catch (NumberFormatException e) {
      System.out.println("You must put in a number for this question.");
      return ask(question, options, valid, incorrect);
    }
    for (int j: valid){
      if (answer == j){
        return answer;
      }
    }
    System.out.println(incorrect);
    return ask(question, options, valid, incorrect);
  }

  public static int ask(String question, int min, int max){
    if (!question.equals("")){
      System.out.println(question);
    }
    String input = new String(sc.next());
    int answer = 0;
    try {
      answer = Integer.parseInt(input);
    } 
    catch (NumberFormatException e) {
      System.out.println("You must put in a number for this question.");
      return ask(question, min, max);
    }
    if (answer > max || answer < min){
      System.out.println("Sorry, that number is not within the allowed range.");
      return ask(question, min, max);
    }
    sc.nextLine();
    return answer;
  }

  public static boolean search(String[] arr, String element) {
    for (String word : arr) {
      if (word.equals(element)) {
        return true;
      }
    }
    return false;
  }

  public static void tell(String[] story, int lineStart, int lines){
    for (int i = lineStart; i <= lines + lineStart; i++){
      read(story[i]);
    }
  }
}