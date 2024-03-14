package a2;

import static utils.TextIO.getln;
import static utils.TextIO.putln;
import static utils.TextIO.writeFile;
import static utils.TextIO.writeStandardOutput;

import utils.DomainConstraint;
import utils.NotPossibleException;

import java.util.Scanner;
import java.util.Vector;

/**
 * @author BaoCuc
 * @overview PCProg is a program that captures data about PC objects
 * and displays a report about them on the console.
 * @attributes objs  Set<PC>
 * @object A typical PCProg is {c1,...,cn} where c1,...,cn are pcs
 * @abstract_properties mutable(objs)=true /\ optional(objs)=false
 */
public class PCProg {
    @DomainConstraint(mutable = true, optional = false)
    private final Set<PC> objs;

    /**
     * @effects initialise this to have an empty set of PCs
     */
    public PCProg() {
        objs = new Set<>();
    }

    public void createObjects() {
        Scanner input = new Scanner(System.in);
        String option1;
        do {
            System.out.print("Enter model: ");
            String model = input.nextLine();

            System.out.print("Enter year: ");
            int year = input.nextInt();
            input.nextLine();

            System.out.print("Enter manufacturer: ");
            String manufacturer = input.nextLine();

            Set<String> components = new Set<>();
            String option2;
            do {
                System.out.print("Enter component: ");
                String comp = input.nextLine();
                if (!components.isIn(comp)) {
                    components.insert(comp);
                } else {
                    System.err.println("Overlapping component!");
                }

                System.out.println("Enter another component? [Y/N]");
                option2 = input.nextLine();
            }
            while (!option2.equalsIgnoreCase("n"));
            PCFactory.getInstance();
            PC pc = PCFactory.createPC(model, year, manufacturer, components);

            if (pc.repOK()) {
                objs.insert(pc);
            }

            System.out.println("Create another PC? [Y/N]");
            option1 = input.nextLine();
        }
        while (!option1.equalsIgnoreCase("n"));
    }

    /**
     * @effects if objs is not empty
     * display to the standard console a text-based tabular report on objs
     * return this report
     * else
     * display nothing and return null
     */
    public String displayReport() {
        if (objs.size() > 0) {
            Vector<PC> pcs = objs.getElements();

            PCReport reportObj = new PCReport();
            return reportObj.displayReport(pcs.toArray(new PC[pcs.size()]));
        } else {
            return null;
        }
    }

    /**
     * @effects save report to a file pcs.txt in the same directory
     * as the program's
     */
    public void saveReport(String report) {
        String fileName = "pcs.txt";
        writeFile(fileName);
        putln(report);
        writeStandardOutput();
    }

    /**
     * The run method
     *
     * @effects initialise an instance of PCProg
     * create objects from data entered by the user
     * display a report on the objects
     * prompt user to save report to file
     * if user answers "Y"
     * save report
     * else
     * end
     */
    public static void main(String[] args) {
        //
        PCProg prog = new PCProg();

        // create objects
        try {
            prog.createObjects();

            // display report
            String report = prog.displayReport();

            if (report != null) {
                // prompt user to save report
                putln("Save report to file? [Y/N]");
                String toSave = getln();
                if (toSave.equals("Y")) {
                    prog.saveReport(report);
                    putln("report saved");
                }
            }
        } catch (NotPossibleException e) {
            System.err.printf("%s: %s%n", e, e.getMessage());
        }

        putln("-END-");
    }
}
