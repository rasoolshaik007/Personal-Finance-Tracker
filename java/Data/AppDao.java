package edu.nwmissouri.personalfinancetracker.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AppDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addUser(User user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addExpenses(Expenses expenses);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addUsersFinanceDetails(FinanceDetails financeDetails);

    @Query("SELECT COUNT(*) FROM user WHERE email = :email")
    LiveData<Integer> checkEmailExists(String email);

    @Query("SELECT * FROM finance_details WHERE email = :email LIMIT 1")
    LiveData<FinanceDetails> getFinanceDetails(String email);

    @Query("SELECT * FROM user WHERE email = :email LIMIT 1")
    LiveData<User> getUserDetails(String email);

    @Query("SELECT * FROM user WHERE email = :email AND password = :password")
    LiveData<User> getUserByEmailAndPassword(String email, String password);

    @Transaction
    default void registerUser(User user) {
        addUser(user);
        addUsersFinanceDetails(new FinanceDetails(user.getEmail(), 0, 0, 0, 0, 0));
    }

    @Query("UPDATE finance_details SET salary = :salary WHERE email = :email")
    void updateSalary(Long salary, String email);

    @Query("UPDATE finance_details SET rd = :rd WHERE email = :email")
    void updateRD(Long rd, String email);

    @Query("UPDATE finance_details SET fd = :fd WHERE email = :email")
    void updateFD(Long fd, String email);

    @Query("UPDATE finance_details SET stock = :stock WHERE email = :email")
    void updateStocks(Long stock, String email);

    @Query("UPDATE user SET firstName = :firstName WHERE email = :email")
    void updateFirstName(String firstName, String email);

    @Query("UPDATE user SET lastName = :lastName WHERE email = :email")
    void updateLastName(String lastName, String email);

    @Query("UPDATE user SET mobile = :mobileNumber WHERE email = :email")
    void updateMobileNumber(String mobileNumber, String email);

    @Query("SELECT * FROM expenses WHERE email = :email ORDER BY date DESC")
    LiveData<List<Expenses>> getExpenses(String email);


    @Query("SELECT * FROM expenses WHERE email = :email AND strftime('%m', date / 1000, 'unixepoch') = :currentMonth AND strftime('%Y', date / 1000, 'unixepoch') = :currentYear ORDER BY id DESC")
    LiveData<List<Expenses>> getExpensesForMonthAndYear(String email, String currentMonth, String currentYear);


}
