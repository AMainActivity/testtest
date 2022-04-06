package ru.ama.ottest.data

import com.google.gson.Gson
import ru.ama.ottest.domain.repository.GameRepository
import ru.ama.ottest.domain.entity.GameSettings
import ru.ama.ottest.domain.entity.MainTest
import ru.ama.ottest.domain.entity.Question
import ru.ama.ottest.domain.entity.Questions
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

object GameRepositoryImpl : GameRepository {

	var countOfQuestion=-1
	val mainTest by lazy{ Gson().fromJson(txt, MainTest::class.java)}
    val questions: List<Questions> by lazy {mainTest.questions}
    /*override fun generateQuestion1(
        maxValue: Int,
        minSumValue: Int,
        minAnswerValue: Int,
        countOfOptions: Int
    ): Question {
        val sum = Random.nextInt(minSumValue, maxValue + 1)
        val visibleNumber = Random.nextInt(minAnswerValue, sum)
        val options = HashSet<Int>()
        val rightAnswer = sum - visibleNumber
        options.add(rightAnswer)
        val from = max(minAnswerValue, rightAnswer - countOfOptions)
        val to = min(rightAnswer + countOfOptions, maxValue)
        while (options.size < countOfOptions) {
            options.add(Random.nextInt(from, to + 1))
        }
        return Question(sum, visibleNumber, options.toList())
    }  */

	override fun getCurrentNoOfQuestion(): Int {
		return countOfQuestion
	}

	override fun getTestInfo(): MainTest {
		return mainTest
	}

	override fun generateQuestion(): Questions {
	/*if (countOfQuestion==-1)
	{
		readQuestions()
	}*/
		if (countOfQuestion==mainTest.questions.size-1)
					countOfQuestion=-1

		countOfQuestion++
	val number=questions[countOfQuestion].number
	val question=questions[countOfQuestion].question
	val imageUrl=questions[countOfQuestion].imageUrl
	val answers=questions[countOfQuestion].answers
	val correct=questions[countOfQuestion].correct
	
	return Questions(number, question, imageUrl,answers,correct)
    }  
	
	/*fun readQuestions(): MainTest {
        val gson = Gson()
		mainTest = Gson().fromJson(txt, MainTest::class.java)
		questions: List<Questions>=mainTest.questions
		//val mainTestType = object: TypeToken<List<MainTest>>(){}.type
		//var persons: List<Person> = gson.fromJson(txt, listPersonType)
    }*/

    override fun getGameSettings(): GameSettings {
		//if (mainTest==null)
		//readQuestions()
        return GameSettings(
                mainTest.minCountOfRightAnswers,
				mainTest.minPercentOfRightAnswers,
				mainTest.testTimeInSeconds
            )
		/*when (level) {
            Level.EASY   -> GameSettings(
                10,
                20,
                80,
                60
            )
            Level.NORMAL -> GameSettings(
                20,
                20,
                85,
                40
            )
            Level.HARD   -> GameSettings(
                30,
                20,
                90,
                30
            )
            Level.TEST   -> GameSettings(
                10,
                3,
                50,
                8
            )*/
        //}
    }
	
	val txt:String="""
	{
"title": "Тест по охране труда",
"mainImageUrl": "",
"minCountOfRightAnswers":8,
"minPercentOfRightAnswers":80,
"testTimeInSeconds":30,
"questions":
[
 {
   "No.": 1,
   "question": " Какое определение понятия «охрана труда» будет верным?", 
   "imageUrl": "", 
   "answers": [
      "Охрана труда - система сохранения жизни и здоровья работников в процессе трудовой деятельности, включающая в себя правовые, социально- экономические, организационно-технические, санитарно-гигиенические, лечебно-профилактические, реабилитационные и иные мероприятия  ",
      "Охрана труда - совокупность факторов производственной среды и трудового процесса, оказывающих влияние на работоспособность и здоровье людей",
      "Охрана труда - это техника безопасности и гигиена труда"
   ],
   "correct": [0]
},
{
   "No.": 2,
   "question": " Что является основными направлениями государственной политики в области охраны труда", 
   "imageUrl": "", 
   "answers": [
      "Обеспечение приоритета сохранения жизни и здоровья работников",
      "Государственное управление охраной труда",
      "Профилактика несчастных случаев и повреждения здоровья работников",
      "Установление гарантий и компенсаций за работу с вредными и (или) опасными условиями труда",
      "Все варианты верны.  "
   ],
   "correct": [4]
},
{
   "No.": 3,
   "question": " Государственные нормативные требования охраны труда обязательны для", 
   "imageUrl": "", 
   "answers": [
      "Юридических лиц при эксплуатации объектов",
      "Юридических и физических лиц при эксплуатации объектов",
      "Юридических и физических лиц при осуществлении ими любых видов деятельности  "
   ],
   "correct": [2]
},
{
   "No.": 4,
   "question": " Кто устанавливает порядок разработки, утверждения и изменения подзаконных нормативных правовых актов, содержащих государственные нормативные требования охраны труда", 
   "imageUrl": "", 
   "answers": [
      "Правительство Российской Федерации",
      "Правительство Российской Федерации с учетом мнения Российской трехсторонней комиссии по регулированию социально-трудовых отношений  ",
      "Правительство Российской Федерации с учетом мнения организации профсоюзов"
   ],
   "correct": [1]
},
{
   "No.": 5,
   "question": " В обязанности работодателя по обеспечению безопасных условий и охраны труда входит", 
   "imageUrl": "", 
   "answers": [
      "Обучение безопасным методам и приемам выполнения работ и оказанию первой помощи пострадавшим на производстве",
      "Организация контроля за состоянием условий труда на рабочих местах",
      "Проведение специальной оценки условий труда",
      "Обеспечение сертифицированных средств индивидуальной и коллективной защиты",
      "Все варианты верны  "
   ],
   "correct": [4]
},
{
   "No.": 6,
   "question": " В каких случаях работники должны проходить обязательные периодические медицинские осмотры?", 
   "imageUrl": "", 
   "answers": [
      "Если работники заняты на работах с вредными и (или) опасными условиями труда (в том числе на подземных работах), а также на работах, связанных с движением транспорта  ",
      "Если работник отсутствовал по болезни на рабочем месте более 3 месяцев",
      "При перерыве в работе более 1 года"
   ],
   "correct": [0]
},
{
   "No.": 7,
   "question": " Цель прохождения предварительных и периодических медицинских осмотров", 
   "imageUrl": "", 
   "answers": [
      "Определение пригодности для выполнения поручаемой работы, предупреждение профессиональных заболеваний  ",
      "Выявление инфекционных заболеваний и направление на лечение",
      "Выявление хронических заболеваний и направление на лечение"
   ],
   "correct": [0]
},
{
   "No.": 8,
   "question": " Обязанности  работника в области охраны труда", 
   "imageUrl": "", 
   "answers": [
      "Соблюдать требования охраны труда",
      "Правильно применять средства индивидуальной и коллективной защиты",
      "Проходить обучение безопасным методам и приемам выполнения работ по охране труда, оказанию первой помощи при несчастных случаях на производстве, инструктаж по охране труда, стажировку на рабочем месте, проверку знаний требований охраны труда",
      "Все варианты верны  "
   ],
   "correct": [3]
},
{
   "No.": 9,
   "question": " О чем работник обязан немедленно известить своего руководителя", 
   "imageUrl": "", 
   "answers": [
      "О любой ситуации, угрожающей жизни и здоровью людей",
      "О каждом несчастном случае, происшедшем на производстве",
      "Об ухудшении состояния своего здоровья",
      "Все варианты верны  "
   ],
   "correct": [3]
},
{
   "No.": 10,
   "question": " Что необходимо для принятия в эксплуатацию новых или реконструируемых производственных объектов", 
   "imageUrl": "", 
   "answers": [
      "Решение налогового органа",
      "Заключение федеральных органов исполнительной власти, осуществляющих функции по контролю и надзору в установленной сфере деятельности  ",
      "Все варианты верны"
   ],
   "correct": [1]
}
]
}
	"""
}
