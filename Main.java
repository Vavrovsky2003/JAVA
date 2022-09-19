import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class StringCalculator{

    //Список роздільників
    private ArrayList<String> Separators = new ArrayList<>();

    //Рядок який нам дають з початку
    private String numbers;

    //Заповнюємо стандартні роздільники
    public StringCalculator(){
        Separators.add("\n");
        Separators.add(",");
    }

    //Додає роздільники в яких є квадратні дужки (обрізаємо їх)
    private void addSeparatorWithSquereHooks(String separator){
        separator = separator.substring(1,separator.length()-1);
        Separators.add(separator);
    }
    //Обрізаємо // з початку і \n з кінця
    private void addSimpleSeparator(String separator){
        separator = separator.substring(2,separator.length()-1);
        Separators.add(separator);
    }
    //Заповняємо список роздільників
    private void checkSeparators(){
        //Стандартно всі роздільники записані у квадратних дужках, отже знайдемо все таке ? робить щоб шукалось співпадіння мінімальної довжини
        Matcher m = Pattern.compile("\\[.*?\\]").matcher(numbers);
        while (m.find())
            addSeparatorWithSquereHooks(m.group());
        //Перевіримо варіант коли в нас роздільник один, без квадратних дужок
        m = Pattern.compile("//.\n").matcher(numbers);
        if (m.find())
            addSimpleSeparator(m.group());
    }
    //Проходимось по списку всіх роздільників і перевіряємо чи наступні символи стрінгу не дають нам хочаб один такий
    private int isSeparator(String a){
        for (String separator: Separators) {
            if(separator.length()<a.length() && separator.equals(a.substring(0,separator.length()))){
                return separator.length();
            }
        }
        return -1;
    }

    //Просумуємо пропускаючи усі роздільники
    private int splitAndSum() throws Exception {
        int sum=0;
        int separatorIndex=0;
        StringBuilder lowerZero = new StringBuilder();
        while (numbers.length()>separatorIndex){
            int len;
            //перевіряємо на наявність роздільника і повертаємо його довжину
            if((len=isSeparator(numbers.substring(separatorIndex)))!=-1) {
                int number;
                //Намагаємось спарсити число
                try {
                    number = Integer.parseInt(numbers.substring(0, separatorIndex));
                } catch (Exception ex) {
                    throw new Exception("Some problems in post separator numbers");
                }
                //Обрізаємо зайве
                numbers = numbers.substring(separatorIndex + len);
                separatorIndex = 0;
                //Перевіримо умови
                if (number < 0) lowerZero.append(number).append(", ");
                else if (number <= 1000) sum += number;
            }
            else separatorIndex++;
        }
        int number;
        try {
            number = Integer.parseInt(numbers);
        } catch (Exception ex) {
            throw new Exception("Some problems in post separator numbers");
        }
        if (number < 0) lowerZero.append(number).append(", ");
        else if (number <= 1000) sum += number;
        if (String.valueOf(lowerZero).isEmpty())
            return sum;
        throw new Exception("Numbers lower zero: "+ lowerZero);
    }

    //Завдяки усім методам утворимо рішення задачі
    int add(String numbers) throws Exception {
        if (numbers.isEmpty())
            return 0;
        this.numbers = numbers;
        checkSeparators();
        if (this.numbers.charAt(0)=='/')
            this.numbers = this.numbers.substring(this.numbers.indexOf('\n')+1);
        Separators.sort((x,y)->y.length()-x.length());

        return splitAndSum();
    }
}

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println(new StringCalculator().add("1,2,3,"));
    }
}