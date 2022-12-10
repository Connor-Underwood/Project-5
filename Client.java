import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

import static javax.swing.JOptionPane.*;

// Client class
public class Client {
    private PrintWriter writer;
    private BufferedReader reader;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    // find some way to differentiate clients in server

    //public static void main(String[] args) {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 1234)) {

            // writing to server
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            // reading from server
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            String[] welcomePage = {"Login", "Create An Account"}; // Welcomes the Client presents drop down menu
            // to login or create account
            String sellerOrCustomer = (String) JOptionPane.showInputDialog(null, "Welcome " +
                            "To Happy Feet!",
                    "Happy Feet", JOptionPane.INFORMATION_MESSAGE, null, welcomePage, null);


            String email; // email of client we use for later
            String password; // password of client we use for later
            String userType = ""; // userType, Customer or Seller
            if (sellerOrCustomer.equals("Login")) {
                writer.println("Login"); // Let Server know Client is trying to log in
                email = JOptionPane.showInputDialog(null, "Enter Your E-Mail", "Happy " +
                        "Feet", JOptionPane.QUESTION_MESSAGE);
                if (email == null) { // If click exit, email will be null, which means we exit program
                    return;
                }
                writer.println(email); // send E-Mail to Server and wait for it to be verified
                while (reader.readLine().equals("Invalid E-Mail")) { // If Invalid E-Mail, run loop to get a Valid One
                    int num = JOptionPane.showConfirmDialog(null,
                            "Invalid E-Mail",
                            "Happy Feet",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.PLAIN_MESSAGE);
                    if (num == JOptionPane.CANCEL_OPTION) { // If they click exit, exit the program
                        return;
                    }

                    // Get new E-Mail
                    email = JOptionPane.showInputDialog(null, "Enter Your E-Mail", "Happy " +
                            "Feet", JOptionPane.QUESTION_MESSAGE);
                    if (email == null) {
                        return;
                    }
                    writer.println(email);
                }
                // At this point, the user has entered a valid E-Mail
                // Now we start the password verification below
                password = JOptionPane.showInputDialog(null, "Enter Your Password", "Happy" +
                        " Feet", JOptionPane.QUESTION_MESSAGE);
                if (password == null) { // Check if exit button, then exit the program
                    return;
                }
                writer.println(password); // Send password to Server and wait for verification
                while (reader.readLine().equals("Invalid Password")) {
                    int num = JOptionPane.showConfirmDialog(null,
                            "Invalid Password",
                            "Happy Feet",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.PLAIN_MESSAGE);
                    if (num == JOptionPane.CLOSED_OPTION) { // If they exit, close program
                        return;
                    }
                    // Get new Password
                    password = JOptionPane.showInputDialog(null, "Enter Your Password",
                            "Happy Feet", JOptionPane.QUESTION_MESSAGE);
                    if (password == null) { // If they exit, close program
                        return;
                    }
                    writer.println(password);
                }
                // At this point, we have valid credentials

                // Receive if the Client is a Customer or Seller fromm the Server Database
                userType = reader.readLine();
            } else if (sellerOrCustomer.equalsIgnoreCase("Create An Account")) {
                writer.println("Create"); // Let Server know Client is trying to Create An Account
                email = JOptionPane.showInputDialog(null, "Please Enter Your E-Mail.");
                if (email == null) { // If they click exit, close program
                    return;
                }
                while (!email.contains("@")) { // A loop to make sure the E-Mail contains an @ character
                    int num = JOptionPane.showConfirmDialog(null, "Invalid E-Mail", "Happy" +
                                    " Feet",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if (num == JOptionPane.CLOSED_OPTION) {
                        return;
                    }
                    email = JOptionPane.showInputDialog(null, "Please Enter Your E-Mail");
                    if (email == null) {
                        return;
                    }
                }
                writer.println(email); // Send to Server for taken E-Mail verification
                while (reader.readLine().equals("Taken")) { // If taken, run a loop until the E-Mail is valid and unique
                    int num = JOptionPane.showConfirmDialog(null, "This E-Mail is Already Taken"
                            , "Happy Feet", JOptionPane.DEFAULT_OPTION,
                            JOptionPane.PLAIN_MESSAGE);
                    if (num == JOptionPane.CLOSED_OPTION) { // If Client exits, close program
                        return;
                    }
                    email = JOptionPane.showInputDialog(null, "Please Enter Your E-Mail");
                    // Receive new E-Mail
                    if (email == null) { // If they exit, close program
                        return;
                    }
                    while (!email.contains("@")) { // Have to check again for the @ character, run a loop
                        num = JOptionPane.showConfirmDialog(null, "Invalid E-Mail", "Happy" +
                                        " Feet",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE);
                        if (num == JOptionPane.CLOSED_OPTION) {
                            return;
                        }
                        email = JOptionPane.showInputDialog(null, "Please Enter Your E-Mail");
                        if (email == null) {
                            return;
                        }
                    }
                    writer.println(email); // At this point, the E-Mail is valid, so we send to Server to our
                    // account can be added to the Server Database
                }

                // Receive Password from Client Input
                password = JOptionPane.showInputDialog(null, "Please Enter A Password Greater" +
                        " Than 5 Characters");
                if (password == null) { // If they exit, close program
                    return;
                }
                while (password.length() < 5) { // Make sure password is greater than 5 characters inside a loop
                    int num = JOptionPane.showConfirmDialog(null, "Password Must Be Greater " +
                                    "Than 5 Characters", "Happy Feet",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if (num == JOptionPane.CLOSED_OPTION) { // If they exit, close program
                        return;
                    }
                    password = JOptionPane.showInputDialog(null, "Please Enter A Password " +
                            "Greater Than 5 Characters");
                    if (password == null) { // If they exit close program
                        return;
                    }
                }
                // Server does not have to verify a new password, so we just send straight to server
                writer.println(password);

                String[] userTypes = {"Seller", "Customer"};
                // Ask if Client is a Customer or Seller
                userType = (String) JOptionPane.showInputDialog(null, "Choose",
                        "Happy Feet", JOptionPane.INFORMATION_MESSAGE, null, userTypes, 0);
                if (userType == null) {
                    return;
                }
                // Send this userType information to the Server to it can be added to the Database
                writer.println(userType);
            }

            if (userType.equals("Seller")) { // Seller Implementation
                String storeName;
                String shoeName;
                JOptionPane.showMessageDialog(null, "Welcome Seller!", "Happy Feet",
                        JOptionPane.PLAIN_MESSAGE);

                int performAnotherActivity;
                String[] sellerMenuOptions = {"Add a Store", "Add a New Shoe", "Remove a Shoe", "Edit a Shoe",
                        "View your sales information", "Change Email", "Change Password", "Import products from a file",
                        "Export products to a file"};

                // PRESENTS SELLER MENU
                do {
                    String chosenOption = (String) JOptionPane.showInputDialog(null, "Select" +
                                    " an Option",
                            "Happy Feet", JOptionPane.INFORMATION_MESSAGE, null, sellerMenuOptions, 0);

                    // SENDS THE CHOSEN OPTION TO THE SERVER
                    writer.println(chosenOption);

                    // ADD STORE
                    if (chosenOption.equalsIgnoreCase("Add a store")) {
                        storeName = JOptionPane.showInputDialog(null, "What is the name of " +
                                "the store you would like to add:", "Happy Feet", JOptionPane.QUESTION_MESSAGE);
                        writer.println(storeName); // SENDS STORENAME TO THE SERVER
                        String addStoreResult = reader.readLine(); // USED TO CHECK IF STORE IS ADDED
                        if (addStoreResult.equalsIgnoreCase("Store added")) {
                            JOptionPane.showMessageDialog(null, "Store added successfully!",
                                    "Happy Feet", JOptionPane.PLAIN_MESSAGE);
                        } else if (addStoreResult.equalsIgnoreCase("You already own this store!")) {
                            JOptionPane.showMessageDialog(null, "You already own the store!",
                                    null, JOptionPane.ERROR_MESSAGE);
                        }
                    }

                    // ADD SHOE
                    if (chosenOption.equalsIgnoreCase("Add a New Shoe")) {
                        ArrayList<String> storeNames = new ArrayList<>();
                        String s = "";
                        while ((s = reader.readLine()) != null) {
                            storeNames.add(s);
                            if (reader.readLine().equalsIgnoreCase("Done")) {
                                break;
                            }
                        }
                        String[] size = new String[storeNames.size()];
                        String[] stores = storeNames.toArray(size);
                        storeName = (String) JOptionPane.showInputDialog(null, "Which store " +
                                "would you like to add" +
                                " the shoe to:", "Happy Feet", JOptionPane.QUESTION_MESSAGE, null, stores, -1);
                        writer.println(storeName); // WE HAVE A PROBLEM HERE (SENDS STORENAME TO THE SERVER)
                        String storeIndex = reader.readLine(); //GETS THE STORE INDEX, FROM THE SERVER TO CHECK
                        // IF STORE EXISTS
                        if (storeIndex.equals("-1")) {
                            JOptionPane.showMessageDialog(null, "You do not own this store!",
                                    "Happy Feet", JOptionPane.ERROR_MESSAGE);
                        } else {
                            shoeName = JOptionPane.showInputDialog(null, "What is the name of " +
                                    "the " +
                                    "shoe you wish to add:", "Happy Feet", JOptionPane.QUESTION_MESSAGE);
                            writer.println(shoeName);
                            String shoeDesc = JOptionPane.showInputDialog(null, "What is the " +
                                    "description of the " +
                                    "shoe you wish to add:", "Happy Feet", JOptionPane.QUESTION_MESSAGE);
                            writer.println(shoeDesc);
                            String price = JOptionPane.showInputDialog(null,
                                    "What is the price of the " +
                                    "shoe you wish to add:", "Happy Feet", JOptionPane.QUESTION_MESSAGE);
                            if (price.contains(".")) {
                                int index = price.indexOf(".");
                                while (price.substring(index + 1).length() > 2) {
                                    JOptionPane.showMessageDialog(null, "Invalid");
                                    price = JOptionPane.showInputDialog(null,
                                            "What is the price of the " +
                                                    "shoe you wish to add:", "Happy Feet", 
                                            JOptionPane.QUESTION_MESSAGE);
                                    index = price.indexOf(".");
                                }
                            }
                            writer.println(price);
                            int quantity = Integer.parseInt(JOptionPane.showInputDialog(null,
                                    "What is the quantity of the " +
                                    "shoe you wish to add:", "Happy Feet", JOptionPane.QUESTION_MESSAGE));
                            writer.println(quantity);
                            String addShoeResult = reader.readLine(); // USED TO CHECK IF SHOE WAS ADDED TO THE STORE
                            if (addShoeResult.equalsIgnoreCase("Shoe added")) {
                                JOptionPane.showMessageDialog(null, "Shoe added successfully!",
                                        "Happy Feet", JOptionPane.PLAIN_MESSAGE);
                            } else if (addShoeResult.equalsIgnoreCase("Shoe could not be added")) {
                                JOptionPane.showMessageDialog(null, "Shoe could not be added :(",
                                        "Happy Feet", JOptionPane.ERROR_MESSAGE);
                            }

                        }
                    }

                    // REMOVE SHOE
                    if (chosenOption.equalsIgnoreCase("Remove a Shoe")) {
                        shoeName = JOptionPane.showInputDialog(null,
                                "What is the name of the shoe you would like to remove?", "Happy Feet",
                                JOptionPane.QUESTION_MESSAGE);
                        writer.println(shoeName);
                        ArrayList<String> stores = new ArrayList<>();
                        String s = "";
                        while ((s = reader.readLine()) != null) {
                            stores.add(s);
                            if (reader.readLine().equalsIgnoreCase("Done")) {
                                break;
                            }
                        }
                        String[] size = new String[stores.size()];
                        String[] storeArray = stores.toArray(size);
                        storeName = (String)JOptionPane.showInputDialog(null,
                                "What store would you like to remove the Shoe from?", "Happy Feet",
                                JOptionPane.QUESTION_MESSAGE, null, storeArray, -1);
                        writer.println(storeName);
                        String storeIndex = reader.readLine(); //GETS THE STORE INDEX, FROM THE SERVER TO CHECK IF
                        // STORE EXISTS
                        if (storeIndex.equals("-1")) {
                            JOptionPane.showMessageDialog(null, "You do not own this store!",
                                    "Happy Feet", JOptionPane.ERROR_MESSAGE);
                        } else {
                            String shoeDescription = JOptionPane.showInputDialog(null, "What " +
                                    "is the " +
                                    "description of the shoe you would like to remove?", "Happy Feet",
                                    JOptionPane.QUESTION_MESSAGE);
                            writer.println(shoeDescription);
                            String shoePrice = JOptionPane.showInputDialog(null, "What is the " +
                                    "price of the shoe you would like to remove?", "Happy Feet",
                                    JOptionPane.QUESTION_MESSAGE);
                            writer.println(shoePrice);
                            String shoeQuantity = JOptionPane.showInputDialog(null,
                                    "What is the quantity of the " +
                                    "shoe you would like to remove?", "Happy Feet", JOptionPane.QUESTION_MESSAGE);
                            writer.println(shoeQuantity);
                            String addShoeResult = reader.readLine(); // USED TO CHECK IF SHOE WAS REMOVED FROM THE
                            // STORE
                            if (addShoeResult.equalsIgnoreCase("Shoe Removed!")) {
                                JOptionPane.showMessageDialog(null, "Shoe removed successfully!",
                                        "Happy Feet", JOptionPane.PLAIN_MESSAGE);
                            } else if (addShoeResult.equalsIgnoreCase(storeName + " does not own " +
                                    shoeName + "'s!")) {
                                JOptionPane.showMessageDialog(null, storeName + " does not own "
                                                + shoeName + "'s!",
                                        "Happy Feet", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }

                    // EDIT SHOE
                    if (chosenOption.equalsIgnoreCase("Edit a Shoe")) {
                        shoeName = JOptionPane.showInputDialog(null, "What shoe do you want " +
                                        "to edit",
                                "Happy Feet", JOptionPane.QUESTION_MESSAGE);
                        writer.println(shoeName);
                        storeName = JOptionPane.showInputDialog(null, "What store does the " +
                                "shoe " +
                                "belong to?", "Happy Feet", JOptionPane.QUESTION_MESSAGE);
                        writer.println(storeName);
                        String storeIndex = reader.readLine();
                        if (storeIndex.equals("-1")) {
                            int num = JOptionPane.showConfirmDialog(null, "You do not own " +
                                            "this store!", "Happy Feet",
                                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE);
                            if (num == CLOSED_OPTION) {
                                return;
                            }
                        } else {
                            String shoeIndex = reader.readLine();
                            if (shoeIndex.equals("-1")) {
                                int num = JOptionPane.showConfirmDialog(null, storeName + " " +
                                                "does not own this shoe!", "Happy Feet",
                                        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE);
                                if (num == CLOSED_OPTION) {
                                    return;
                                }
                            } else {
                                int choice = JOptionPane.showConfirmDialog(null, "Do you want" +
                                                " to change the name of the Shoe?",
                                        "Happy Feet", JOptionPane.YES_NO_OPTION);
                                if (choice == YES_OPTION) {
                                    writer.println("Change Shoe Name");
                                    shoeName = JOptionPane.showInputDialog(null, "What do you" +
                                            " want the name of the shoe to be?");
                                    while (shoeName == null) {
                                        shoeName = JOptionPane.showInputDialog(null, "What do " +
                                                "you want the name of the shoe to be?");
                                    }
                                    writer.println(shoeName);
                                } else {
                                    writer.println("");
                                }
                                choice = JOptionPane.showConfirmDialog(null, "Do you want to " +
                                        "change " +
                                        "the description of the Shoe?", "Happy Feet", YES_NO_OPTION);
                                if (choice == YES_OPTION) {
                                    writer.println("Change Shoe Description");
                                    String shoeDescription = JOptionPane.showInputDialog(null,
                                            "What do you want the " +
                                            "description of the Shoe to be?");
                                    while (shoeDescription == null) {
                                        shoeDescription = JOptionPane.showInputDialog(null,
                                                "What do you want the " +
                                                "description of the Shoe to be?");
                                    }
                                    writer.println(shoeDescription);
                                } else {
                                    writer.println("");
                                }
                                choice = JOptionPane.showConfirmDialog(null, "Do you want to " +
                                        "change " +
                                        "the price of the Shoe?", "Happy Feet", YES_NO_OPTION);
                                if (choice == YES_OPTION) {
                                    writer.println("Change Shoe Price");
                                    String shoePrice = JOptionPane.showInputDialog(null, "What " +
                                            "do you want the " +
                                            "price of the Shoe to be?");
                                    if (shoePrice.contains(".")) {
                                        int index = shoePrice.indexOf(".");
                                    while (shoePrice == null || shoePrice.substring(index + 1).length() > 2) {
                                        shoePrice = JOptionPane.showInputDialog(null, "What " +
                                                "do you want the " +
                                                "price of the Shoe to be? (2 Decimal Places)");
                                        }
                                    }
                                    writer.println(shoePrice);
                                } else {
                                    writer.println("");
                                }
                                choice = JOptionPane.showConfirmDialog(null, "Do you want to " +
                                        "change the " +
                                        "quantity of the Shoe?", "Happy Feet", YES_NO_OPTION);
                                if (choice == YES_OPTION) {
                                    writer.println("Change Shoe Quantity");
                                    String shoeQuantity = JOptionPane.showInputDialog(null, "" +
                                            "What do you want " +
                                            "the quantity of the Shoe to be?");
                                    while (shoeQuantity == null) {
                                        shoeQuantity = JOptionPane.showInputDialog(null, "What " +
                                                "do you want " +
                                                "the quantity of the Shoe to be?");
                                    }
                                    writer.println(shoeQuantity);
                                } else {
                                    writer.println("");
                                }
                                String valid = reader.readLine();
                                int num;
                                if (valid.equals("Y")) {
                                    num = JOptionPane.showConfirmDialog(null, "Shoe " +
                                                    "Successfully Edited!",
                                            "Happy Feet", DEFAULT_OPTION, PLAIN_MESSAGE);
                                } else {
                                    num = JOptionPane.showConfirmDialog(null, "Shoe Could Not " +
                                                    "Be Edited",
                                            "Happy Feet", DEFAULT_OPTION, PLAIN_MESSAGE);
                                }
                                if (num == CLOSED_OPTION) {
                                    return;
                                }
                            }
                        }
                    }

                    // VIEW SALES
                    if (chosenOption.equalsIgnoreCase("View your sales information")) {
                        String[] arr = {"Sort by Customer Sales", "Sort by Store Sales", "No Filters"};
                        String option = (String)JOptionPane.showInputDialog(null, "Sort " +
                                        "the DashBoard", "Happy Feet", JOptionPane.QUESTION_MESSAGE, null,
                                arr, -1);
                        if (option.equalsIgnoreCase("no filters")) {
                            writer.println("none");
                            String s = (String) ois.readObject();
                            JOptionPane.showMessageDialog(null, s);
                        } else if (option.equalsIgnoreCase("sort by store sales")) {
                            writer.println("store");
                            String s = (String) ois.readObject();
                            JOptionPane.showMessageDialog(null, s);
                        } else if (option.equalsIgnoreCase("sort by customer sales")) {
                            writer.println("customer");
                            String s = (String) ois.readObject();
                            JOptionPane.showMessageDialog(null, s);
                        }
                    }
                    // CHANGE E-MAIL
                    if (chosenOption.equalsIgnoreCase("Change Email")) {
                        String newEmail = JOptionPane.showInputDialog(null, "Enter your " +
                                "new Email:");
                        while (!MarketPlace.checkEmail(newEmail)) {
                            newEmail = JOptionPane.showInputDialog(null, "Enter your " +
                                    "new Email:");
                        }
                        writer.println(newEmail);
                    }

                    if (chosenOption.equalsIgnoreCase("Change Password")) {
                        String newPass;
                        while (true) {
                            newPass = JOptionPane.showInputDialog(null, "What do you " +
                                    "want your new password to be?");
                            if (newPass.length() < 5) {
                                JOptionPane.showMessageDialog(null, "Password must be " +
                                        "greater than 5 characters!", "Happy Feet", JOptionPane.ERROR_MESSAGE);
                                continue;
                            }
                            break;
                        }
                        writer.println(newPass);
                    }

                    if (chosenOption.equalsIgnoreCase("Import products from a file")) {
                        String input = JOptionPane.showInputDialog(null, "Enter the file path");
                        writer.println(input);
                        String success = reader.readLine();
                        if (success.equalsIgnoreCase("true")) {
                            JOptionPane.showMessageDialog(null, "Your products have been " +
                                    "successfully " +
                                    "imported to the Market!");
                        } else {
                            JOptionPane.showMessageDialog(null, "Unable to import your" +
                                    " products to " +
                                    "the market.");
                        }
                    }

                    if (chosenOption.equalsIgnoreCase("Export products to a file")) {
                        String input = JOptionPane.showInputDialog(null, "Enter the name " +
                                "of the file you would " +
                                "like to see your products in.");
                        writer.println(input);
                        String success = reader.readLine();
                        if (success.equalsIgnoreCase("true")) {
                            JOptionPane.showMessageDialog(null, "Your products have been " +
                                    "successfully exported to " + input);
                        } else {
                            JOptionPane.showMessageDialog(null, "Unable to export products" +
                                    " to File Name: " + input);
                        }
                    }

                    performAnotherActivity = JOptionPane.showConfirmDialog(null,
                            "Would you like to perform another activity", "Happy Feet",
                            JOptionPane.YES_NO_OPTION);
                    writer.println(performAnotherActivity);
                } while (performAnotherActivity == YES_OPTION);  //LOOPS OVER SELLER MENU AGAIN IF CLIENT SELECTS YES


            } else { // Customer Implementation
                String storeName;
                String shoeName;
                JOptionPane.showMessageDialog(null, "Welcome Customer!", "Happy Feet",
                        JOptionPane.PLAIN_MESSAGE);

                int performAnotherActivity;
                String[] customerMenuOptions = {MarketPlace.VIEW_MARKET, MarketPlace.SEARCH_MARKET,
                        MarketPlace.REVIEW_PURCHASE_HISTORY, MarketPlace.EXPORT_SHOE,
                        MarketPlace.CHANGE_CUSTOMER_EMAIL, MarketPlace.CHANGE_CUSTOMER_PASSWORD,
                        MarketPlace.PURCHASE_SHOE, MarketPlace.VIEW_MARKET_STATISTICS};

                // PRESENTS CUSTOMER MENU

                do {
                    String chosenOption = (String) JOptionPane.showInputDialog(null,
                            "Select an Option",
                            "Happy Feet", JOptionPane.INFORMATION_MESSAGE, null,
                            customerMenuOptions, 0);

                    // SENDS THE CHOSEN OPTION TO THE SERVER
                    writer.println(chosenOption);
                    if (chosenOption.equalsIgnoreCase(MarketPlace.VIEW_MARKET)) {
                        Object[] cols = {"Seller ID", "Store Name", "Shoe Name", "Shoe Price"};
                        ArrayList<ArrayList<String>> marketList = new ArrayList<>();
                        ArrayList<String> shoe = new ArrayList<>();
                        String response;
                        while (!(reader.readLine().equals("done writing"))) {
                            while (!((response = reader.readLine())).equals("done")) {
                                shoe.add(response);
                            }
                            marketList.add(shoe);
                            shoe = new ArrayList<>();
                        }

                        if (marketList.size() == 0) {
                            JOptionPane.showMessageDialog(null, "No shoes in the market!",
                                    "Happy Feet", ERROR_MESSAGE);
                        } else {
                            Object[][] rows = new Object[marketList.size()][4];
                            for (int i = 0; i < marketList.size(); i++) {
                                for (int j = 0; j < marketList.get(i).size(); j++) {
                                    rows[i][j] = marketList.get(i).get(j);
                                }
                            }
                            JTable table = new JTable(rows, cols);
                            JOptionPane.showMessageDialog(null, new JScrollPane(table));
                        }
                        int purchaseChoice = JOptionPane.showConfirmDialog(null,
                                "Do you want to purchase any of those shoes?");
                        if (purchaseChoice == YES_OPTION) {
                            writer.println("wants to purchase");
                            if (marketList.size() == 0) {
                                JOptionPane.showMessageDialog(null, "No shoes in the market!",
                                        "Happy Feet", ERROR_MESSAGE);
                            } else {
                                ArrayList<String> s = new ArrayList<>();
                                for (int i = 0; i < marketList.size(); i++) {
                                    String string = "";
                                    for (int j = 0; j < marketList.get(i).size(); j++) {
                                        if (j == 0) {
                                            string += "Seller ID: " + marketList.get(i).get(j) + " | ";
                                        } else if (j == 1) {
                                            string += "Store Name: " + marketList.get(i).get(j) + " | ";
                                        } else if (j == 2) {
                                            string += "Shoe Name: " + marketList.get(i).get(j) + " | ";
                                        } else {
                                            string += "Shoe Price: $" + marketList.get(i).get(j);
                                        }
                                    }
                                    s.add(string);
                                }
                                String[] strings = new String[s.size()];
                                String[] arr = s.toArray(strings);
                                String input = (String) JOptionPane.showInputDialog(null,
                                        "Purchase a shoe.", "Happy Feet",
                                        INFORMATION_MESSAGE, null, arr, -1);
                                String sellerID = input.substring(input.indexOf(":") + 1,input.indexOf("|"));
                                sellerID = sellerID.trim();
                                input = input.substring(input.indexOf("|") + 1,input.length()-1);
                                writer.println(sellerID);

                                String store= input.substring(input.indexOf(":") + 1,input.indexOf("|"));
                                store = store.trim();
                                input = input.substring(input.indexOf("|") + 1,input.length()-1);
                                writer.println(store);

                                String wantedShoeName = input.substring(input.indexOf(":") + 1,input.indexOf("|"));
                                wantedShoeName = wantedShoeName.trim();
                                writer.println(wantedShoeName);

                                String shoeInfo = (String) ois.readObject();
                                JOptionPane.showMessageDialog(null, shoeInfo);
                                String quantity = JOptionPane.showInputDialog(null, "How many " +
                                        "pairs would you like to purchase");
                                boolean validResponse = false;
                                do {
                                    try {
                                        Integer.parseInt(quantity);
                                        if (Integer.parseInt(quantity) < 1) {
                                            JOptionPane.showMessageDialog(null, "Invalid Value.");
                                        } else if (Integer.parseInt(quantity) > Integer.parseInt(shoeInfo.split(
                                                ":")[5].trim())) {
                                            JOptionPane.showMessageDialog(null, "Sorry, " +
                                                    "we do not have that many pairs on stock");
                                        } else {
                                            validResponse = true;
                                        }

                                    } catch (NumberFormatException n) {
                                        JOptionPane.showMessageDialog(null, MarketPlace.INVALID_VALUE,
                                                "Happy Feet", ERROR_MESSAGE);
                                    }
                                    if(!validResponse) {
                                        quantity = JOptionPane.showInputDialog(null, "How many " +
                                                "pairs would you like to purchase?");
                                    }
                                } while (!validResponse);
                                writer.println(quantity);
                                JOptionPane.showMessageDialog(null, reader.readLine());
                            }
                        }

                    } else if (chosenOption.equalsIgnoreCase(MarketPlace.SEARCH_MARKET)) {
                        ArrayList<ArrayList<String>> marketList = new ArrayList<>();
                        ArrayList<String> shoe = new ArrayList<>();
                        String response;
                        Object[] cols = {"Seller ID", "Store Name", "Shoe Name", "Shoe Price"};
                        String[] viewOptions = {"Search by Store Name.", "Search by Shoe Name.", "Search by Shoe" +
                                " Description.", "Sort by Price.", "Sort by Quantity"};
                        String searchChoice = (String) JOptionPane.showInputDialog(null, "Se" +
                                "lect an Option", "Happy Feet", INFORMATION_MESSAGE, null, viewOptions,
                                0);
                        writer.println(searchChoice);
                        if (searchChoice.equalsIgnoreCase("Search by Store Name.")) {
                            String store = JOptionPane.showInputDialog(null, "What is the " +
                                    "name of the store?");
                            writer.println(store);
                            while (!(reader.readLine().equals("done writing"))) {
                                while (!((response = reader.readLine())).equals("done")) {
                                    shoe.add(response);
                                }
                                marketList.add(shoe);
                                shoe = new ArrayList<>();
                            }
                            if (marketList.size() == 0) {
                                JOptionPane.showMessageDialog(null, "No shoes in the market!",
                                        "Happy Feet", ERROR_MESSAGE);
                            } else {
                                Object[][] rows = new Object[marketList.size()][4];
                                for (int i = 0; i < marketList.size(); i++) {
                                    for (int j = 0; j < marketList.get(i).size(); j++) {
                                        rows[i][j] = marketList.get(i).get(j);
                                    }
                                }
                                JTable table = new JTable(rows, cols);
                                JOptionPane.showMessageDialog(null, new JScrollPane(table));
                            }
                        } else if (searchChoice.equalsIgnoreCase("Search by Shoe Name.")) {
                            String name = JOptionPane.showInputDialog(null, "What is the " +
                                    "name of the shoe?");
                            writer.println(name);
                            while (!(reader.readLine().equals("done writing"))) {
                                while (!((response = reader.readLine())).equals("done")) {
                                    shoe.add(response);
                                }
                                marketList.add(shoe);
                                shoe = new ArrayList<>();
                            }
                            if (marketList.size() == 0) {
                                JOptionPane.showMessageDialog(null, "No shoes in the market!",
                                        "Happy Feet", ERROR_MESSAGE);
                            } else {
                                Object[][] rows = new Object[marketList.size()][4];
                                for (int i = 0; i < marketList.size(); i++) {
                                    for (int j = 0; j < marketList.get(i).size(); j++) {
                                        rows[i][j] = marketList.get(i).get(j);
                                    }
                                }
                                JTable table = new JTable(rows, cols);
                                JOptionPane.showMessageDialog(null, new JScrollPane(table));
                            }
                        } else if (searchChoice.equalsIgnoreCase("Search by Shoe Description.")) {
                            String Description = JOptionPane.showInputDialog(null, "What is" +
                                    " the description of the shoe?");
                            writer.println(Description);
                            while (!(reader.readLine().equals("done writing"))) {
                                while (!((response = reader.readLine())).equals("done")) {
                                    shoe.add(response);
                                }
                                marketList.add(shoe);
                                shoe = new ArrayList<>();
                            }
                            if (marketList.size() == 0) {
                                JOptionPane.showMessageDialog(null, "No shoes in the market!",
                                        "Happy Feet", ERROR_MESSAGE);
                            } else {
                                Object[][] rows = new Object[marketList.size()][4];
                                for (int i = 0; i < marketList.size(); i++) {
                                    for (int j = 0; j < marketList.get(i).size(); j++) {
                                        rows[i][j] = marketList.get(i).get(j);
                                    }
                                }
                                JTable table = new JTable(rows, cols);
                                JOptionPane.showMessageDialog(null, new JScrollPane(table));
                            }
                        } else if (searchChoice.equalsIgnoreCase("Sort by price.")) {
                            String Price = JOptionPane.showInputDialog(null, "What is the " +
                                    "price you want to sort by?");
                            boolean validResponse = false;
                            do {
                                try {
                                    Double.parseDouble(Price);
                                    validResponse = true;
                                } catch (NumberFormatException n) {
                                    Price = JOptionPane.showInputDialog(null, "What is the " +
                                            "price you want to sort by?");
                                }
                            } while (!validResponse);
                            writer.println(Price);
                            while (!(reader.readLine().equals("done writing"))) {
                                while (!((response = reader.readLine())).equals("done")) {
                                    shoe.add(response);
                                }
                                marketList.add(shoe);
                                shoe = new ArrayList<>();
                            }
                            if (marketList.size() == 0) {
                                JOptionPane.showMessageDialog(null, "No shoes in the market!",
                                        "Happy Feet", ERROR_MESSAGE);
                            } else {
                                Object[][] rows = new Object[marketList.size()][4];
                                for (int i = 0; i < marketList.size(); i++) {
                                    for (int j = 0; j < marketList.get(i).size(); j++) {
                                        rows[i][j] = marketList.get(i).get(j);
                                    }
                                }
                                JTable table = new JTable(rows, cols);
                                JOptionPane.showMessageDialog(null, new JScrollPane(table));
                            }
                        } else if (searchChoice.equalsIgnoreCase("sort by quantity")) {
                            String quantity = JOptionPane.showInputDialog(null, "What is" +
                                    " quantity you want to sort by?");
                            boolean validResponse = false;
                            do {
                                try {
                                    Integer.parseInt(quantity);
                                    validResponse = true;
                                } catch (NumberFormatException n) {
                                    JOptionPane.showMessageDialog(null, MarketPlace.INVALID_VALUE,
                                            "Happy Feet", ERROR_MESSAGE);
                                    quantity = JOptionPane.showInputDialog(null, "What is" +
                                            " quantity you want to sort by?");
                                }
                            } while (!validResponse);
                            writer.println(quantity);
                            while (!(reader.readLine().equals("done writing"))) {
                                while (!((response = reader.readLine())).equals("done")) {
                                    shoe.add(response);
                                }
                                marketList.add(shoe);
                                shoe = new ArrayList<>();
                            }
                            if (marketList.size() == 0) {
                                JOptionPane.showMessageDialog(null, "No shoes in the market!",
                                        "Happy Feet", ERROR_MESSAGE);
                            } else {
                                Object[][] rows = new Object[marketList.size()][4];
                                for (int i = 0; i < marketList.size(); i++) {
                                    for (int j = 0; j < marketList.get(i).size(); j++) {
                                        rows[i][j] = marketList.get(i).get(j);
                                    }
                                }
                                JTable table = new JTable(rows, cols);
                                JOptionPane.showMessageDialog(null, new JScrollPane(table));
                            }
                        }
                        int purchaseChoice = JOptionPane.showConfirmDialog(null, "Do you" +
                                " want to purchase any of those shoes?");
                        if (purchaseChoice == YES_OPTION) {
                            writer.println("wants to purchase");
                            if (marketList.size() == 0) {
                                JOptionPane.showMessageDialog(null, "No shoes in the market!",
                                        "Happy Feet", ERROR_MESSAGE);
                            } else {
                                ArrayList<String> s = new ArrayList<>();
                                for (int i = 0; i < marketList.size(); i++) {
                                    String string = "";
                                    for (int j = 0; j < marketList.get(i).size(); j++) {
                                        if (j == 0) {
                                            string += "Seller ID: " + marketList.get(i).get(j) + " | ";
                                        } else if (j == 1) {
                                            string += "Store Name: " + marketList.get(i).get(j) + " | ";
                                        } else if (j == 2) {
                                            string += "Shoe Name: " + marketList.get(i).get(j) + " | ";
                                        } else {
                                            string += "Shoe Price: $" + marketList.get(i).get(j);
                                        }
                                    }
                                    s.add(string);
                                }
                                String[] strings = new String[s.size()];
                                String[] arr = s.toArray(strings);
                                String input = (String) JOptionPane.showInputDialog(null,
                                        "Purchase a shoe.", "Happy Feet",
                                        INFORMATION_MESSAGE, null, arr, -1);
                                String sellerID = input.substring(input.indexOf(":") + 1,input.indexOf("|"));
                                sellerID = sellerID.trim();
                                input = input.substring(input.indexOf("|") + 1,input.length()-1);
                                writer.println(sellerID);

                                String store= input.substring(input.indexOf(":") + 1,input.indexOf("|"));
                                store = store.trim();
                                input = input.substring(input.indexOf("|") + 1,input.length()-1);
                                writer.println(store);

                                String wantedShoeName = input.substring(input.indexOf(":") + 1,input.indexOf("|"));
                                wantedShoeName = wantedShoeName.trim();
                                writer.println(wantedShoeName);

                                String shoeInfo = (String) ois.readObject();
                                JOptionPane.showMessageDialog(null, shoeInfo);
                                String quantity = JOptionPane.showInputDialog(null, "How " +
                                        "many pairs would you like to purchase");
                                boolean validResponse = false;
                                do {
                                    try {
                                        Integer.parseInt(quantity);
                                        if (Integer.parseInt(quantity) < 1) {
                                            JOptionPane.showMessageDialog(null, "Invalid Value.");
                                        } else if (Integer.parseInt(quantity) > Integer.parseInt(shoeInfo.split(
                                                ":")[5].trim())) {
                                            JOptionPane.showMessageDialog(null, "Sorry, we" +
                                                    " do not have that many pairs on stock");
                                        } else {
                                            validResponse = true;
                                        }

                                    } catch (NumberFormatException n) {
                                        JOptionPane.showMessageDialog(null, MarketPlace.INVALID_VALUE,
                                                "Happy Feet", ERROR_MESSAGE);
                                    }
                                    if(!validResponse) {
                                        quantity = JOptionPane.showInputDialog(null, "How" +
                                                " many pairs would you like to purchase?");
                                    }
                                } while (!validResponse);
                                writer.println(quantity);
                                JOptionPane.showMessageDialog(null, reader.readLine());
                            }
                        }

                    } else if (chosenOption.equalsIgnoreCase(MarketPlace.REVIEW_PURCHASE_HISTORY)) {
                        String result = (String) ois.readObject();
                        if (result.startsWith("Total")) {
                            JOptionPane.showMessageDialog(null, result);
                        } else {
                            JOptionPane.showMessageDialog(null, result, "Happy Feet", ERROR_MESSAGE);
                        }

                    } else if (chosenOption.equalsIgnoreCase(MarketPlace.EXPORT_SHOE)) {
                        String input = JOptionPane.showInputDialog(null, "What is the name " +
                                "of the file you like to see your purchase history in?");
                        writer.println(input);
                        String result = reader.readLine();
                        if (result.startsWith("Total")) {
                            JOptionPane.showMessageDialog(null, result);
                        } else {
                            JOptionPane.showMessageDialog(null, result, "Happy Feet", ERROR_MESSAGE);
                        }

                    } else if (chosenOption.equalsIgnoreCase(MarketPlace.CHANGE_CUSTOMER_EMAIL)) {
                        String newEmail = JOptionPane.showInputDialog(null, "Enter" +
                                " your new Email:");
                        while (!MarketPlace.checkEmail(newEmail)) {
                            newEmail = JOptionPane.showInputDialog(null, "Enter your n" +
                                    "ew Email:");
                        }
                        writer.println(newEmail);

                    } else if (chosenOption.equalsIgnoreCase(MarketPlace.CHANGE_CUSTOMER_PASSWORD)) {
                        String newPass;
                        while (true) {
                            newPass = JOptionPane.showInputDialog(null, "What do you want " +
                                    "your new password to be?");
                            if (newPass.length() < 5) {
                                JOptionPane.showMessageDialog(null, "Password must be " +
                                        "greater than 5 characters!", "Happy Feet", JOptionPane.ERROR_MESSAGE);
                                continue;
                            }
                            break;
                        }
                        writer.println(newPass);

                    } else if (chosenOption.equalsIgnoreCase(MarketPlace.PURCHASE_SHOE)) {
                        ArrayList<ArrayList<String>> marketList = new ArrayList<>();
                        ArrayList<String> shoe = new ArrayList<>();
                        String response;
                        while (!(reader.readLine().equals("done writing"))) {
                            while(!((response = reader.readLine())).equals("done")) {
                                shoe.add(response);
                            }
                            marketList.add(shoe);
                            shoe = new ArrayList<>();
                        }


                        if (marketList.size() == 0) {
                            JOptionPane.showMessageDialog(null, "No shoes in the market!",
                                    "Happy Feet", ERROR_MESSAGE);
                        } else {
                            ArrayList<String> s = new ArrayList<>();
                            for (int i = 0; i < marketList.size(); i++) {
                                String string = "";
                                for (int j = 0; j < marketList.get(i).size(); j++) {
                                    if (j == 0) {
                                        string += "Seller ID: " + marketList.get(i).get(j) + " | ";
                                    } else if (j == 1) {
                                        string += "Store Name: " + marketList.get(i).get(j) + " | ";
                                    } else if (j == 2) {
                                        string += "Shoe Name: " + marketList.get(i).get(j) + " | ";
                                    } else {
                                        string += "Shoe Price: $" + marketList.get(i).get(j);
                                    }
                                }
                                s.add(string);
                            }
                            String[] strings = new String[s.size()];
                            String[] arr = s.toArray(strings);

                            String input = (String) JOptionPane.showInputDialog(null, "" +
                                            "Purchase a shoe.", "Happy Feet",
                                    INFORMATION_MESSAGE, null, arr, -1);
                            String sellerID = input.substring(input.indexOf(":") + 1,input.indexOf("|"));
                            sellerID = sellerID.trim();
                            input = input.substring(input.indexOf("|") + 1,input.length()-1);
                            writer.println(sellerID);

                            String store= input.substring(input.indexOf(":") + 1,input.indexOf("|"));
                            store = store.trim();
                            input = input.substring(input.indexOf("|") + 1,input.length()-1);
                            writer.println(store);

                            String wantedShoeName = input.substring(input.indexOf(":") + 1,input.indexOf("|"));
                            wantedShoeName = wantedShoeName.trim();
                            writer.println(wantedShoeName);

                            String shoeInfo = (String) ois.readObject();
                            JOptionPane.showMessageDialog(null, shoeInfo);
                            String quantity = JOptionPane.showInputDialog(null, "How many" +
                                    " pairs would you like to purchase");
                            boolean validResponse = false;
                            do {
                                try {
                                    Integer.parseInt(quantity);
                                    if (Integer.parseInt(quantity) < 1) {
                                        JOptionPane.showMessageDialog(null, "Invalid Value.");
                                    } else if (Integer.parseInt(quantity) > Integer.parseInt(shoeInfo.split(
                                            ":")[5].trim())) {
                                        JOptionPane.showMessageDialog(null, "Sorry, we d" +
                                                "o not have that many pairs on stock");
                                    } else {
                                        validResponse = true;
                                    }

                                } catch (NumberFormatException n) {
                                    JOptionPane.showMessageDialog(null, MarketPlace.INVALID_VALUE,
                                            "Happy Feet", ERROR_MESSAGE);
                                }
                                if(!validResponse) {
                                    quantity = JOptionPane.showInputDialog(null, "How many" +
                                            " pairs would you like to purchase?");
                                }
                            } while (!validResponse);
                            writer.println(quantity);
                            JOptionPane.showMessageDialog(null, reader.readLine());
                        }

                    } else if (chosenOption.equalsIgnoreCase(MarketPlace.VIEW_MARKET_STATISTICS)) {
                        int sort = JOptionPane.showConfirmDialog(null, "Would you like to sort " +
                                "the dashboard?", "Happy Feet", YES_NO_OPTION);
                        if (sort == YES_OPTION) {
                            String[] options = {"Sort by number of products sold in every store",
                                    "Sort by number of products sold in stores you have purchased from"};
                            String sortBy = (String) JOptionPane.showInputDialog(null,
                                    "Select an Option",
                                    "Happy Feet", JOptionPane.INFORMATION_MESSAGE, null, options,
                                    0);
                            if (sortBy.equals("Sort by number of products sold in every store")) {
                                writer.println("Sort by number of products sold in every store");
                                String result = (String) ois.readObject();
                                JOptionPane.showMessageDialog(null, result);
                            } else {
                                writer.println("Sort by number of products sold in stores you have purchased from");
                                String result = (String) ois.readObject();
                                JOptionPane.showMessageDialog(null, result);
                            }
                        } else {
                            writer.println("No");
                            String result = (String) ois.readObject();
                            JOptionPane.showMessageDialog(null, result);
                        }
                    }
                    performAnotherActivity = JOptionPane.showConfirmDialog(null,
                            "Would you like to perform another activity", "Happy Feet",
                            JOptionPane.YES_NO_OPTION);
                    writer.println(performAnotherActivity);
                } while (performAnotherActivity == YES_OPTION);
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
