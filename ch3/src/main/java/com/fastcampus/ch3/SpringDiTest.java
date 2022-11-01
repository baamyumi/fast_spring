//package com.fastcampus.ch3;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.support.GenericXmlApplicationContext;
//import org.springframework.stereotype.Component;
//
//import java.util.Arrays;
//
////@Component("engine")
//class Engine{}
//@Component class SuperEngine extends Engine{}
//@Component class TurboEngine extends Engine{}
//@Component class Door{}
//
//@Component class Car{
//    @Value("red") String color;
//    @Value("100") int oil;
//    @Autowired
//    @Qualifier("superEngine")   //타입명 앞 대문자를 소문자로 바꾼 이름이 없을때 선택될 타입을 지정
//    Engine engine;   //@Autowired의 특성 : 타입으로 먼저 검색, 여러개면 이름으로 검색
//    @Autowired Door[] doors;
//
//    public Car(){}
//    public Car(String color, int oil, Engine engine, Door[] doors) {
//        this.color = color;
//        this.oil = oil;
//        this.engine = engine;
//        this.doors = doors;
//    }
//
//    public void setColor(String color) {
//        this.color = color;
//    }
//
//    public void setOil(int oil) {
//        this.oil = oil;
//    }
//
//    public void setEngine(Engine engine) {
//        this.engine = engine;
//    }
//
//    public void setDoors(Door[] doors) {
//        this.doors = doors;
//    }
//
//    @Override
//    public String toString() {
//        return "Car{" +
//                "color='" + color + '\'' +
//                ", oil=" + oil +
//                ", engine=" + engine +
//                ", doors=" + Arrays.toString(doors) +
//                '}';
//    }
//}
//
//
//public class SpringDiTest {
//    public static void main(String[] args) {
//        ApplicationContext ac = new GenericXmlApplicationContext("config.xml");
//        //Car car = (Car)ac.getBean("car");   //ByName  아래와 같은 문장
//        Car car = ac.getBean("car", Car.class);
//        Car car2 = (Car)ac.getBean(Car.class);   //byType
//        //Engine engine = (Engine)ac.getBean("engine"); //ByName
//        Engine engine = (Engine)ac.getBean("superEngine"); //ByType
//        Door door = (Door)ac.getBean("door");
//
////        car.setColor("red");
////        car.setOil(100);
////        car.setEngine(engine);
////        car.setDoors(new Door[]{ac.getBean(("door"), Door.class), ac.getBean(("door"), Door.class)});
//        System.out.println("car = " + car);
//    }
//}
