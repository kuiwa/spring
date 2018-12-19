package com.how2java.pojo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.hibernate.usertype.UserCollectionType;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.how2java.pojo.Product;

public class TestHibernate {

    SessionFactory sf;
    Session s;

    @BeforeClass
    public void beforeClass() {
        sf = new Configuration().configure().buildSessionFactory();
        s = sf.openSession();
        // s = sf.getCurrentSession();
        s.beginTransaction();
    }

    @AfterClass
    public void afterClass() {
        s.getTransaction().commit();
        s.close();
        sf.close();
    }

    //    @Test
    public void runTestA() {
        Product p = new Product();
        p.setName("iphone7");
        p.setPrice(7000);
        s.save(p);

        Product p2 = new Product();
        p2.setName("Huawei P20");
        p2.setPrice(4000);
        s.save(p2);

        Category c = new Category();
        c.setName("Category 1");
        s.save(c);
    }

    //    @Test
    public void runTestB() {
        Map<String, Integer> map = new HashMap();
        map.put("Huawei P20", 4000);
        map.put("Iphone X", 8000);

        map.forEach((k, v) -> {
            Product p = new Product();
            p.setName(k);
            p.setPrice(v);
            s.save(p);
        });
    }

    //    @Test
    public void runGet() {
        Product product = (Product) s.get(Product.class, 6);
        System.out.println("id = 6, product name = " + product.getName());
    }

    //        @Test
    public void runDelete() {
        Product product = (Product) s.get(Product.class, 7);
        System.out.println("Delete id = 10, product name = " + product.getName());
        s.delete(product);
    }

    //    @Test
    public void runDeleteAll() {
        for (int i = 20; i <= 65; i++) {
            Product product = (Product) s.get(Product.class, i);
            System.out.println("Delete id = 10, product name = " + product.getName());
            s.delete(product);
        }
    }

    //    @Test
    public void runModify() {
        Product product = (Product) s.get(Product.class, 3);
        System.out.println("Modify id = 3, product name = " + product.getName());
        product.setName("Iphone XI");
        product.setPrice(10000);
        s.update(product);
    }

    //    @Test
    public void runHQLQuery() {
        String name = "Iphone";
        String queryCommand = "from Product p where p.name like ?";
        Query query = s.createQuery(queryCommand);
        query.setString(0, "%" + name + "%");
        List<Product> ps = query.list();
        ps.forEach((p) -> System.out.println(p.getName()));

        Iterator<Product> iterator = query.iterate();
        iterator.forEachRemaining((p) -> System.out.println(p.getName()));

        String sqlQueryCommandString = "select * from product_ p where p.name like '%" + name + "%'";
        Query query2 = s.createSQLQuery(sqlQueryCommandString);
        List<Object[]> qList = query2.list();
        qList.forEach((q) -> {
            for (Object os : q) {
                System.out.println(os + "\t");
            }

        });
    }

    //    @Test
    public void runCriteria() {
        String name = "Huawei";
        Criteria criteria = s.createCriteria(Product.class);
        criteria.add(Restrictions.like("name", "%" + name + "%"));
        List<Product> ps = criteria.list();
        ps.forEach((p) -> System.out.println(p.getName()));
    }

    //    @Test
    public void runCriteriaSetResult() {
        String name = "Huawei";
        Criteria criteria = s.createCriteria(Product.class);
        criteria.add(Restrictions.like("name", "%" + name + "%"));
        criteria.setFirstResult(0);
        criteria.setMaxResults(20);

        List<Product> ps = criteria.list();
        ps.forEach((p) -> System.out.println(p.getName()));
    }

    //    @Test
    public void testAddCategory() {
        Category category = new Category();
        category.setName("c1");
        s.save(category);

        Product product = (Product) s.get(Product.class, 16);
        product.setCategory(category);
        s.update(product);
    }

    //    @Test
    public void testCategoryToProduct() {
        Category category2 = (Category) s.get(Category.class, 1);
        Product product = (Product) s.get(Product.class, 17);
        product.setCategory(category2);
        s.update(product);
    }

    //    @Test
    public void testGetCategory() {
        Category category = (Category) s.get(Category.class, 1);
        Set<Product> pSet = category.getProducts();
        pSet.forEach((p) -> System.out.println(p.getName()));
    }

    //    @Test
    public void testUsersToProduct() {
        User user1 = new User();
        user1.setName("Sarah");
        s.save(user1);

        User user2 = new User();
        user2.setName("Michael");
        s.save(user2);

        Set<User> users = new HashSet<>();
        users.add(user1);
        users.add(user2);

        Product product = (Product) s.get(Product.class, 14);
        product.setUsers(users);
        s.update(product);
    }

    //    @Test
    public void testLoad() {
        Product product = (Product) s.load(Product.class, 14);
        System.out.println("Log1");
        System.out.println(product.getName());
        System.out.println("Log2");
    }

    //    @Test
    public void testLazyLoad() {
        Category category = (Category) s.get(Category.class, 1);
        System.out.println("LogA");
        System.out.println(category.getProducts());
        System.out.println("LogB");
    }

    //    @Test
    public void testDeleteManyToMany() {
        Category category = (Category) s.get(Category.class, 2);
        s.delete(category);
    }

    @Test
    public void testUniqueResult() {
        String name = "Huawei";
        Query query = s.createQuery("select count(*) from Product p where p.name like ?");
        query.setString(0, "%" + name + "%");
        long total = (Long) query.uniqueResult();
        System.out.println(total);
    }

    //    @Test
    public void testVersion() {
        Session s1 = sf.openSession();
        Session s2 = sf.openSession();
        s1.beginTransaction();
        s2.beginTransaction();

        Product product1 = (Product) s1.get(Product.class, 15);
        System.out.println("Prince before update: " + product1.getPrice());
        product1.setPrice(product1.getPrice() + 1000);

        Product product2 = (Product) s2.get(Product.class, 15);
        product2.setPrice(product2.getPrice() + 1000);

        s1.update(product1);
        s2.update(product2);

        s1.getTransaction().commit(); // move it after s1.update while version feature takes effect.
        s2.getTransaction().commit();

        Product product = (Product) s.get(Product.class, 15);
        System.out.println("Price after twice raising: " + product.getPrice());

        s1.close();
        s2.close();

    }
}
