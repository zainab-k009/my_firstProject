
package cinenova;
import java.util.Scanner;


public class CineNova {

    static Scanner sc = new Scanner(System.in);

    static String[] movieName     = {"Avatar", "Inception", "Dune", "Interstellar"};
    static String[] movieGenre    = {"Sci-Fi", "Thriller", "Adventure", "Sci-Fi"};
    static String[] movieLanguage = {"English", "English", "English", "English"};
    static double[] moviePrice    = {800, 700, 750, 700};

    static int[]    showMovie = {0, 0, 1, 2, 3, 1};
    static String[] showDate  = {"10 October", "10 October", "11 October",
                                 "11 October", "12 October", "12 October"};
    static String[] showTime  = {"6:00 PM", "9:00 PM", "3:00 PM",
                                 "6:00 PM", "8:00 PM", "9:30 PM"};

    static String[] hallName  = {"Hall 1", "Hall 2", "Hall 3"};
    static String[] hallType  = {"Standard", "Premium", "Standard"};
    static int[]    hallSeats = {30, 40, 20};

    static boolean[][][] seatBooked = new boolean[showMovie.length][hallName.length][40];
    static final double REGULAR_SEAT_PRICE = 300;
    static final double PREMIUM_SEAT_PRICE = 500;
    static final double VIP_MULTIPLIER     = 1.5;   
    static final double VIP_EXTRA_CHARGES  = 200;
    static final double REFUND_PERCENT     = 80;    

    static String[] snackName  = {"Popcorn", "Nachos", "Cold Drink", "Fries"};
    static double[] snackPrice = {300, 350, 150, 250};

    static String[] comboName  = {"Movie Combo", "Family Combo", "Snack Combo"};
    static String[] comboItems = {"Popcorn + Nachos + Cold Drink",
                                  "2 Popcorn + 2 Cold Drink + Fries",
                                  "Fries + Cold Drink"};
    static double[] comboPrice = {1000, 1800, 350};

    static final int MAX = 100;
    static int bookingCount = 0;
    static int nextBookingId = 1050;
    static int nextCustomerId = 1;
    static int nextTicketId = 1;
    static int nextPaymentId = 1;
    static int nextReceiptId = 1;

    static int[]    bId       = new int[MAX];
    static String[] bCustomer = new String[MAX];
    static int[]    bMovie    = new int[MAX];
    static int[]    bShow     = new int[MAX];
    static int[]    bHall     = new int[MAX];
    static int[]    bSeat     = new int[MAX];
    static double[] bPaid     = new double[MAX];
    static String[] bStatus   = new String[MAX];   

    static int reviewCount = 0;
    static String[] rName      = new String[MAX];
    static int[]    rMovie     = new int[MAX];
    static int[]    rRating    = new int[MAX];
    static String[] rText      = new String[MAX];
    static String[] rRecommend = new String[MAX];

    static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                int value = Integer.parseInt(line);
                if (value >= min && value <= max) return value;
            } catch (NumberFormatException e) {
                
            }
            System.out.println("  Invalid input. Enter a number from " + min + " to " + max + ".");
        }
    }
    
    static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double value = Double.parseDouble(sc.nextLine().trim());
                if (value >= 0) return value;
            } catch (NumberFormatException e) {
                
            }
            System.out.println("  Invalid amount. Enter a positive number.");
        }
    }

    static String readText(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            if (!s.isEmpty()) return s;
            System.out.println("  This field cannot be empty.");
        }
    }

    static String readPhone() {
        while (true) {
            String p = readText("Phone Number (11 digits): ");
            if (p.matches("\\d{11}")) return p;
            System.out.println("  Phone number must contain exactly 11 digits.");
        }
    }

    
    static String readEmail() {
        while (true) {
            String e = readText("Email: ");
            if (e.contains("@") && e.indexOf('@') > 0 && e.indexOf('.', e.indexOf('@')) > 0)
                return e;
            System.out.println("  Please enter a valid email (example: ali@gmail.com).");
        }
    }

    static void line() {
        System.out.println("------------------------------------------------------------");
    }

    static String seatType(int hall, int seatNo) {
        return (seatNo <= hallSeats[hall] * 0.6) ? "Regular" : "Premium";
    }

    static double seatPrice(String type) {
        return type.equals("Premium") ? PREMIUM_SEAT_PRICE : REGULAR_SEAT_PRICE;
    }    
    static int availableInHall(int show, int hall) {
        int count = 0;
        for (int i = 0; i < hallSeats[hall]; i++)
            if (!seatBooked[show][hall][i]) count++;
        return count;
    }
    static int availableInShow(int show) {
        int total = 0;
        for (int h = 0; h < hallName.length; h++) total += availableInHall(show, h);
        return total;
    }    
    static int selectMovie() {
        System.out.println("\n=== MOVIE SELECTION ===");
        for (int i = 0; i < movieName.length; i++) {
            System.out.printf("%d. %-14s Genre: %-10s Language: %-8s Base Price: Rs. %.0f%n",
                    i + 1, movieName[i], movieGenre[i], movieLanguage[i], moviePrice[i]);
        }
        return readInt("Choose movie: ", 1, movieName.length) - 1;
    }
    static int selectShow(int movie) {
        System.out.println("\n=== SHOW TIME SELECTION: " + movieName[movie] + " ===");
        int[] options = new int[showMovie.length];
        int n = 0;
        for (int s = 0; s < showMovie.length; s++) {
            if (showMovie[s] == movie) {
                options[n++] = s;
                System.out.printf("%d. %-12s %-8s Available Seats: %d%n",
                        n, showDate[s], showTime[s], availableInShow(s));
            }
        }
        if (n == 0) return -1;
        int choice = readInt("Choose show: ", 1, n);
        int show = options[choice - 1];
        if (availableInShow(show) == 0) {
            System.out.println("  Sorry, this show is fully booked.");
            return -1;
        }
        return show;
    }    
    static int selectHall(int show) {
        System.out.println("\n=== CINEMA HALL SELECTION ===");
        for (int h = 0; h < hallName.length; h++) {
            System.out.printf("%d. Hall ID: %d | %s | Type: %-8s | Total Seats: %d | Available: %d%n",
                    h + 1, h + 1, hallName[h], hallType[h], hallSeats[h], availableInHall(show, h));
        }
        while (true) {
            int h = readInt("Choose hall: ", 1, hallName.length) - 1;
            if (availableInHall(show, h) > 0) return h;
            System.out.println("  That hall is full. Please choose another one.");
        }
    }    
    static int selectSeat(int show, int hall) {
        System.out.println("\n=== SEAT SELECTION ===");
        System.out.printf("Regular seats: Rs. %.0f | Premium seats (last 40%%): Rs. %.0f%n",
                REGULAR_SEAT_PRICE, PREMIUM_SEAT_PRICE);
        System.out.println("[XX] = already booked\n");
        for (int s = 1; s <= hallSeats[hall]; s++) {
            if (seatBooked[show][hall][s - 1]) System.out.print("[XX] ");
            else System.out.printf("[%2d] ", s);
            if (s % 10 == 0) System.out.println();
        }
        System.out.println();
        while (true) {
            int seat = readInt("Enter seat number: ", 1, hallSeats[hall]);
            if (!seatBooked[show][hall][seat - 1]) return seat;
            System.out.println("  Seat " + seat + " is already booked. Try another seat.");
        }
    }
    static double selectSnacks() {
        System.out.println("\n=== SNACKS SELECTION ===");
        double total = 0;
        while (true) {
            for (int i = 0; i < snackName.length; i++)
                System.out.printf("%d. %-12s Rs. %.0f%n", i + 1, snackName[i], snackPrice[i]);
            System.out.println("0. Done with snacks");
            int choice = readInt("Choose snack: ", 0, snackName.length);
            if (choice == 0) break;
            int qty = readInt("Quantity (1-20): ", 1, 20);
            double cost = snackPrice[choice - 1] * qty;
            total += cost;
            System.out.printf("  Added %d x %s = Rs. %.0f%n", qty, snackName[choice - 1], cost);
        }
        System.out.printf("Total Snack Price: Rs. %.0f%n", total);
        return total;
    }    
    static double selectCombos() {
        System.out.println("\n=== FOOD COMBO SELECTION ===");
        double total = 0;
        while (true) {
            for (int i = 0; i < comboName.length; i++)
                System.out.printf("%d. %-13s (%s) Rs. %.0f%n",
                        i + 1, comboName[i], comboItems[i], comboPrice[i]);
            System.out.println("0. Done with combos");
            int choice = readInt("Choose combo: ", 0, comboName.length);
            if (choice == 0) break;
            int qty = readInt("Quantity (1-10): ", 1, 10);
            double cost = comboPrice[choice - 1] * qty;
            total += cost;
            System.out.printf("  Added %d x %s = Rs. %.0f%n", qty, comboName[choice - 1], cost);
        }
        System.out.printf("Total Combo Price: Rs. %.0f%n", total);
        return total;
    }    
    
    static double discountPercent(String customerType, int age) {
        if (customerType.equals("Student")) return 10;
        if (customerType.equals("Senior") || age >= 60) return 15;
        return 0;
    }

        static double makePayment(double totalAmount) {
        System.out.println("\n=== PAYMENT ===");
        System.out.println("1. Cash\n2. Credit/Debit Card\n3. JazzCash\n4. EasyPaisa");
        int m = readInt("Choose payment method: ", 1, 4);
        String[] methods = {"Cash", "Card", "JazzCash", "EasyPaisa"};
        String method = methods[m - 1];

        double paid = 0;
        while (paid < totalAmount) {
            System.out.printf("Total Amount: Rs. %.0f | Paid so far: Rs. %.0f | Remaining: Rs. %.0f%n",
                    totalAmount, paid, totalAmount - paid);
            double amt = readDouble("Enter amount to pay (0 to abort payment): ");
            if (amt == 0) break;
            paid += amt;
        }
        double remaining = Math.max(0, totalAmount - paid);
        String status = (remaining == 0) ? "Completed" : "Pending";

        System.out.println("\nPayment ID      : " + (nextPaymentId++));
        System.out.println("Payment Method  : " + method);
        System.out.printf("Total Amount    : Rs. %.0f%n", totalAmount);
        System.out.printf("Amount Paid     : Rs. %.0f%n", paid);
        System.out.printf("Remaining Amount: Rs. %.0f%n", remaining);
        System.out.println("Payment Status  : " + status);
        if (paid > totalAmount)
            System.out.printf("Change returned : Rs. %.0f%n", paid - totalAmount);
        return Math.min(paid, totalAmount);
        }
    
    static void newBooking() {
        
        System.out.println("\n=== CUSTOMER INFORMATION ===");
        int customerId = nextCustomerId++;
        String name  = readText("Customer Name: ");
        int age      = readInt("Age: ", 5, 120);
        String phone = readPhone();
        String email = readEmail();        
        int movie = selectMovie();
        int show = selectShow(movie);
        if (show == -1) { System.out.println("Booking stopped."); return; }
        int hall = selectHall(show);
        int seatNo = selectSeat(show, hall);
        String sType = seatType(hall, seatNo);
        double sPrice = seatPrice(sType);

        System.out.printf("%nSeat Number: %d | Seat Type: %s | Seat Price: Rs. %.0f | Seat Status: Available%n",
                seatNo, sType, sPrice);
        System.out.println("\n=== TICKET TYPE ===");
        System.out.println("1. Normal Ticket\n2. VIP Ticket (higher price + extra charges)");
        int t = readInt("Choose ticket type: ", 1, 2);
        String ticketType = (t == 1) ? "Normal" : "VIP";
        double ticketPrice = (t == 1) ? moviePrice[movie] : moviePrice[movie] * VIP_MULTIPLIER;
        double extra = (t == 1) ? 0 : VIP_EXTRA_CHARGES;
        double ticketTotal = ticketPrice + extra + sPrice;   

        System.out.println("Ticket ID: " + (nextTicketId++));
        System.out.printf("Ticket Price: Rs. %.0f | Extra Charges: Rs. %.0f | Seat: Rs. %.0f%n",
                ticketPrice, extra, sPrice);
        System.out.printf("Total Ticket Price: Rs. %.0f%n", ticketTotal);        
        double snackTotal = selectSnacks() + selectCombos();        
        System.out.println("\n=== DISCOUNT ===");
        System.out.println("1. Regular\n2. Student (10%)\n3. Senior (15%)");
        int c = readInt("Customer type: ", 1, 3);
        String[] types = {"Regular", "Student", "Senior"};
        String customerType = types[c - 1];

        double totalAmount = ticketTotal + snackTotal;
        double pct = discountPercent(customerType, age);
        double discount = totalAmount * pct / 100.0;
        double finalAmount = totalAmount - discount;

        System.out.println("Customer Type   : " + customerType);
        System.out.printf("Total Amount    : Rs. %.0f%n", totalAmount);
        System.out.printf("Discount        : %.0f%%%n", pct);
        System.out.printf("Discount Amount : Rs. %.0f%n", discount);
        System.out.printf("Final Amount    : Rs. %.0f%n", finalAmount);        
        double paid = makePayment(finalAmount);
        if (paid < finalAmount) {
            System.out.println("\nPayment incomplete. Booking NOT confirmed and seat not reserved.");
            return;
        }
        if (bookingCount >= MAX) { System.out.println("Booking limit reached."); return; }
        seatBooked[show][hall][seatNo - 1] = true;
        int id = nextBookingId++;
        bId[bookingCount]       = id;
        bCustomer[bookingCount] = name;
        bMovie[bookingCount]    = movie;
        bShow[bookingCount]     = show;
        bHall[bookingCount]     = hall;
        bSeat[bookingCount]     = seatNo;
        bPaid[bookingCount]     = paid;
        bStatus[bookingCount]   = "Confirmed";
        bookingCount++;

        System.out.println("\n=== BOOKING CONFIRMATION ===");
        System.out.println("Booking ID     : " + id);
        System.out.println("Customer Name  : " + name + " (ID " + customerId + ")");
        System.out.println("Movie Name     : " + movieName[movie]);
        System.out.println("Show           : " + showDate[show] + ", " + showTime[show]);
        System.out.println("Hall           : " + hallName[hall] + " (" + hallType[hall] + ")");
        System.out.println("Seat Number    : " + seatNo + " (" + sType + ")");
        System.out.println("Booking Status : Confirmed");

        
        printReceipt(name, phone, email, movie, show, hall, seatNo, ticketType,
                ticketTotal, snackTotal, discount, finalAmount, id);
    }
    static void printReceipt(String name, String phone, String email, int movie, int show,
                             int hall, int seat, String ticketType, double ticketTotal,
                             double snackTotal, double discount, double finalAmount, int bookingId) {
        System.out.println();
        line();
        System.out.println("                  CINENOVA - FINAL RECEIPT");
        line();
        System.out.println("Receipt ID    : " + (nextReceiptId++));
        System.out.println("Booking ID    : " + bookingId);
        System.out.println("Customer Name : " + name);
        System.out.println("Phone / Email : " + phone + " / " + email);
        System.out.println("Movie         : " + movieName[movie]);
        System.out.println("Show          : " + showDate[show] + ", " + showTime[show]);
        System.out.println("Hall / Seat   : " + hallName[hall] + " / " + seat);
        System.out.println("Ticket Type   : " + ticketType);
        line();
        System.out.printf("Ticket Total  : Rs. %,.0f%n", ticketTotal);
        System.out.printf("Snack Total   : Rs. %,.0f%n", snackTotal);
        System.out.printf("Discount      : Rs. %,.0f%n", discount);
        System.out.printf("Final Amount  : Rs. %,.0f%n", finalAmount);
        line();
        System.out.println("       Thank you for choosing CineNova. Enjoy the movie!");
        line();
    }

    static int findBooking(int id) {
        for (int i = 0; i < bookingCount; i++)
            if (bId[i] == id) return i;
        return -1;
    }

    static void viewBookings() {
        System.out.println("\n=== ALL BOOKINGS ===");
        if (bookingCount == 0) { System.out.println("No bookings yet."); return; }
        for (int i = 0; i < bookingCount; i++) {
            System.out.printf("ID %d | %s | %s | %s %s | %s Seat %d | Paid Rs. %.0f | %s%n",
                    bId[i], bCustomer[i], movieName[bMovie[i]], showDate[bShow[i]],
                    showTime[bShow[i]], hallName[bHall[i]], bSeat[i], bPaid[i], bStatus[i]);
        }
    }

    
    static void cancelBooking() {
        System.out.println("\n=== CANCELLATION / REFUND ===");
        int id = readInt("Enter Booking ID: ", 0, 999999);
        int i = findBooking(id);
        if (i == -1) { System.out.println("No booking found with ID " + id); return; }
        if (bStatus[i].equals("Cancelled")) {
            System.out.println("This booking is already cancelled.");
            return;
        }
        String reason = readText("Cancellation Reason: ");
        double refund = bPaid[i] * REFUND_PERCENT / 100.0;

        
        seatBooked[bShow[i]][bHall[i]][bSeat[i] - 1] = false;
        bStatus[i] = "Cancelled";

        System.out.println("\nBooking ID          : " + id);
        System.out.println("Customer Name       : " + bCustomer[i]);
        System.out.println("Movie               : " + movieName[bMovie[i]]);
        System.out.println("Cancellation Reason : " + reason);
        System.out.printf("Refund Amount       : Rs. %.0f (%.0f%% of Rs. %.0f paid)%n",
                refund, REFUND_PERCENT, bPaid[i]);
        System.out.println("Cancellation Status : Completed");
    }

    
    static void addReview() {
        System.out.println("\n=== MOVIE REVIEW ===");
        if (reviewCount >= MAX) { System.out.println("Review storage full."); return; }
        String name = readText("Customer Name: ");
        int movie = selectMovie();
        int rating = readInt("Rating (1-5): ", 1, 5);
        String text = readText("Short review: ");
        String rec = "";
        while (!rec.equals("Yes") && !rec.equals("No")) {
            String in = readText("Recommend to others? (yes/no): ").toLowerCase();
            if (in.equals("yes") || in.equals("y")) rec = "Yes";
            else if (in.equals("no") || in.equals("n")) rec = "No";
            else System.out.println("  Please type yes or no.");
        }
        rName[reviewCount] = name;
        rMovie[reviewCount] = movie;
        rRating[reviewCount] = rating;
        rText[reviewCount] = text;
        rRecommend[reviewCount] = rec;
        reviewCount++;
        System.out.println("Thank you for your feedback, " + name + "!");
    }

    static void viewReviews() {
        System.out.println("\n=== MOVIE REVIEWS ===");
        if (reviewCount == 0) { System.out.println("No reviews yet."); return; }
        for (int m = 0; m < movieName.length; m++) {
            int count = 0, sum = 0;
            for (int i = 0; i < reviewCount; i++)
                if (rMovie[i] == m) { count++; sum += rRating[i]; }
            if (count == 0) continue;
            System.out.printf("%n%s - Average rating: %.1f/5 (%d reviews)%n",
                    movieName[m], (double) sum / count, count);
            for (int i = 0; i < reviewCount; i++)
                if (rMovie[i] == m)
                    System.out.printf("  %s: %d/5 - \"%s\" (Recommends: %s)%n",
                            rName[i], rRating[i], rText[i], rRecommend[i]);
        }
    }

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("   CINENOVA - Cinema Experience & Ticket Booking System");
        System.out.println("============================================================");

        boolean running = true;
        while (running) {
            System.out.println("\n----------------- MAIN MENU -----------------");
            System.out.println("1. New Booking");
            System.out.println("2. View All Bookings");
            System.out.println("3. Cancel Booking / Refund");
            System.out.println("4. Add Movie Review");
            System.out.println("5. View Movie Reviews");
            System.out.println("0. Exit");
            int choice = readInt("Enter your choice: ", 0, 5);

            switch (choice) {
                case 1: newBooking();    break;
                case 2: viewBookings();  break;
                case 3: cancelBooking(); break;
                case 4: addReview();     break;
                case 5: viewReviews();   break;
                case 0:
                    running = false;
                    System.out.println("Thank you for using CineNova. Goodbye!");
                    break;
            }
        }
        sc.close();
    }
}