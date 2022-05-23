package classes.rooms;

public class DTR extends Room {
    public DTR(long id) {
        super(id);
        maxSize = 5;
    }

    @Override
    public boolean updateGameStage() {
        if (gameStage % 2 == 0) {
            //Получить фото
            //Разослать фото
        }
        else if (gameStage < 6) {
            //Получить оценки
            //Разослать задания
        }
        else {
            //Разослать результаты
            return false;
        }
        gameStage++;
        return true;
    }
}
