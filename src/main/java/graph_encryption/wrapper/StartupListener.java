//package graph_encryption.wrapper;
//
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.event.ContextRefreshedEvent;
//import org.springframework.stereotype.Component;
//
//@Component
//public class StartupListener implements ApplicationListener<ContextRefreshedEvent> {
//    private static boolean once = true;
//
//    @Override
//    public void onApplicationEvent(ContextRefreshedEvent event) {
//        if (once) {
//            once = false;
//
//            try {
//                ApplicationContext applicationContext = event.getApplicationContext();
//            } catch (Exception e) {
//            }
//        }
//    }
//
//}
