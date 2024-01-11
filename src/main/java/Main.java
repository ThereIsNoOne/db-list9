import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;

import java.util.List;

public class Main {

    private static EntityManagerFactory factory;
    private static EntityManager manager;

    public static void main(String[] args) {
        factory = Persistence.createEntityManagerFactory("default");
        manager = factory.createEntityManager();

        ex1();
        ex2();
        ex3();
        ex4();
        ex5();
        ex6();
    }

    private static void ex1() {

        String query = "SELECT 100.0 * COUNT(p) / ((SELECT COUNT(p2) FROM ProductsEntity p2)) FROM ProductsEntity p WHERE p.calcium + p.iron > 50";
        Query queryA = manager.createQuery(query);
        float resultA = (float) queryA.getSingleResult();

        System.out.printf("a) Percent of products fulfilling requirements: %.2f %%\n\n", resultA);
    }

    private static void ex2() {

        String query = "SELECT AVG(p.calories) FROM ProductsEntity p WHERE p.itemName LIKE :arg";

        Query queryB = manager.createQuery(query);
        queryB.setParameter("arg", "%bacon%");
        double result = (double) queryB.getSingleResult();
        System.out.printf("b) Average value of calories for products with bacon: %.2f\n\n", result);
    }

    private static void ex3() {

        System.out.println("c) Max calories for each category:");
        String query = "SELECT c.catName, max(p.cholesterole) FROM ProductsEntity p JOIN p.category c GROUP BY c.catName";
        Query queryC = manager.createQuery(query);

        List<Object[]> resultList = queryC.getResultList();
        for (Object[] row : resultList) {
            System.out.printf("Category %s -> %s Calories\n", row[0], row[1]);
        }
        System.out.println();
    }

    private static void ex4() {
        String query = "SELECT COUNT(p.itemName) FROM ProductsEntity p WHERE p.itemName LIKE :coffee or p.itemName LIKE :mocha AND p.fiber = 0";
        Query queryD = manager.createQuery(query);
        queryD.setParameter("coffee", "%coffee%");
        queryD.setParameter("mocha", "%mocha%");

        long result = (long) queryD.getSingleResult();
        System.out.printf("d) Coffees without fiber: %d\n", result);
    }

    private static void ex5() {
        System.out.println("e) Calories per McMuffin:");
        String query = "SELECT p.itemName, p.calories FROM ProductsEntity p WHERE p.itemName LIKE :mcmuffin";
        Query queryE = manager.createQuery(query);
        queryE.setParameter("mcmuffin", "%mcmuffin%");

        ((List<Object[]>)queryE.getResultList()).stream()
                .forEach(item -> System.out.printf("%s has %.4f kJ\n", item[0], (double)(int)item[1]/4184));
    }


    private static void ex6() {
        System.out.println("f) Distinct carbs values:");

        String query = "SELECT COUNT(DISTINCT(p.carbs)) FROM ProductsEntity p";
        Query queryF = manager.createQuery(query);
        ((List<Long>) queryF.getResultList()).stream().forEach(item -> System.out.printf("Carbs: %d\n", item));
    }


}
