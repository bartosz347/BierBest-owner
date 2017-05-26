package bierbest.order;

import bierbest.view.GetOrdersTask;
import bierbest.MainApp;
import bierbest.view.MainScreenView;
import javafx.collections.ObservableList;

public class OrdersTableViewModel {

    private MainScreenView mainScreenView;

    public OrdersTableViewModel(MainScreenView mainScreenView) {
        this.mainScreenView = mainScreenView;
    }

    public void load(boolean showRejected) {
        final GetOrdersTask getOrdersTask = new GetOrdersTask(MainApp.sessionFactory, showRejected);
        getOrdersTask.setOnSucceeded(ev -> mainScreenView.setTableItems((ObservableList<OrderViewModel>) getOrdersTask.getValue()));
        Thread backgroundThread = new Thread(getOrdersTask);
        backgroundThread.setDaemon(true);
        backgroundThread.start();
    }

}
