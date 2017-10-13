import java.util.*;

public class Main {
    private static List<Basket> basketList;
    private static int[] itemSupportList = new int[101];
    private static HashMap<Integer, TransactionPair> transactionPairMap = new HashMap<>();
    private static List<AprioriPass> aprioriPassList = new ArrayList<>();
    private static int passNumber = 2;
    private static final int THRESHOLD = 5;

    public static void main(String[] args) {

        basketList = generateBucketForExcerciseB(100);
        calculateItemSupport();
        showItemSupport();
        calculateItemPairOccuredCount();
        calculateItemSupportForFirstPass();
        goThroughAllPass();
    }

    private static void displayOutputForAllPass() {
        for (AprioriPass aprioriPass : aprioriPassList) {
            displayOutputForPass(aprioriPass);
        }
    }

    private static void displayOutputForPass(AprioriPass aprioriPass) {
        for (AprioriTransactionPair aprioriTransactionPair : aprioriPass.getAprioriTransactionPairList()) {
            System.out.print("{");
            for (int pairIndex = 0; pairIndex < aprioriTransactionPair.getPairList().size(); pairIndex++) {
                if (pairIndex != 0) {
                    System.out.print(",");
                }
                System.out.print(aprioriTransactionPair.getPairList().get(pairIndex));
            }
            System.out.print("}");
            System.out.print("=>" + aprioriTransactionPair.getPairOccuredCount());
            System.out.print("\t");
        }
    }

    private static void goThroughAllPass() {
        boolean isNextPassRequired = true;
        while (isNextPassRequired) {
            isNextPassRequired = false;
            AprioriPass currentPass = aprioriPassList.get(aprioriPassList.size() - 1);
            AprioriPass aprioriPass = new AprioriPass(passNumber++);
            for (int aprioriTransactionPairIndex1 = 0; aprioriTransactionPairIndex1 < currentPass.getAprioriTransactionPairList().size(); aprioriTransactionPairIndex1++) {
                for (int aprioriTransactionPairIndex2 = aprioriTransactionPairIndex1 + 1; aprioriTransactionPairIndex2 < currentPass.getAprioriTransactionPairList().size(); aprioriTransactionPairIndex2++) {
                    AprioriTransactionPair aprioriTransactionPair1 = currentPass.getAprioriTransactionPairList().get(aprioriTransactionPairIndex1);
                    AprioriTransactionPair aprioriTransactionPair2 = currentPass.getAprioriTransactionPairList().get(aprioriTransactionPairIndex2);
                    List<Integer> aprioriSubList1 = aprioriTransactionPair1.getPairList().subList(0, aprioriTransactionPair1.getPairList().size() - 1);
                    List<Integer> aprioriSubList2 = aprioriTransactionPair2.getPairList().subList(0, aprioriTransactionPair2.getPairList().size() - 1);
                    if (aprioriSubList1.equals(aprioriSubList2)) {
                        AprioriTransactionPair newAprioriTransactionPair = new AprioriTransactionPair();
                        newAprioriTransactionPair.addAllPairList(aprioriSubList1);
                        newAprioriTransactionPair.addPair(aprioriTransactionPair1.getPairList().subList(aprioriTransactionPair1.getPairList().size() - 1, aprioriTransactionPair1.getPairList().size()).get(0), aprioriTransactionPair2.getPairList().subList(aprioriTransactionPair2.getPairList().size() - 1, aprioriTransactionPair2.getPairList().size()).get(0));
                        List<Integer> aprioriSublist3 = newAprioriTransactionPair.getPairList().subList(1, newAprioriTransactionPair.getPairList().size());
                        boolean isSublist3IsInList = false;
                        int minItemSupport = aprioriTransactionPair1.getPairOccuredCount();
                        if (aprioriTransactionPair2.getPairOccuredCount() < minItemSupport) {
                            minItemSupport = aprioriTransactionPair2.getPairOccuredCount();
                        }
                        for (AprioriTransactionPair aprioriTransactionPair : currentPass.getAprioriTransactionPairList()) {
                            if (aprioriTransactionPair.getPairList().equals(aprioriSublist3)) {
                                if (aprioriTransactionPair.getPairOccuredCount() < minItemSupport) {
                                    minItemSupport = aprioriTransactionPair.getPairOccuredCount();
                                }
                                isSublist3IsInList = true;

                            }
                        }
                        if (isSublist3IsInList) {
                            newAprioriTransactionPair.setPairOccuredCount(minItemSupport);
                            aprioriPass.addAprioriTransactionPair(newAprioriTransactionPair);
                            isNextPassRequired = true;
                        }
                    }
                }
            }

            // All Dataset
            System.out.println();
            System.out.println();
            System.out.println("C" + (passNumber - 1) + " - Candidate pairs of size " + (passNumber - 1));
            displayOutputForPass(aprioriPass);
            removeInFrquentItemSet(aprioriPass);
            aprioriPassList.add(aprioriPass);
            // All Frequent Dataset
            System.out.println();
            System.out.println("L" + (passNumber - 1) + " - Truly frequent ItemSets of size " + (passNumber - 1) + " => Threshold is " + THRESHOLD);
            displayOutputForPass(aprioriPass);
        }
    }

    private static void removeInFrquentItemSet(AprioriPass aprioriPass) {
        Iterator<AprioriTransactionPair> iter = aprioriPass.getAprioriTransactionPairList().iterator();
        while (iter.hasNext()) {
            AprioriTransactionPair aprioriTransactionPair = iter.next();
            if (aprioriTransactionPair.getPairOccuredCount() < THRESHOLD) {
                iter.remove();
//                aprioriPass.removeAprioriTransactionPair(aprioriTransactionPair);
            }
        }
    }


    public static List<Basket> generateBucket(int max) {
        List<Basket> basketList = new ArrayList<>();
        for (int i = 1; i <= max; i++) {
            Basket basket = new Basket(i);
            for (int j = 1; j <= i; j++) {
                if (i % j == 0) {
                    basket.getItemList().add(j);
                }
            }
            basketList.add(basket);
        }
        return basketList;
    }

    public static List<Basket> generateBucketForExcerciseB(int max) {
        List<Basket> basketList = new ArrayList<>();
        for (int i = 1; i <= max; i++) {
            Basket basket = new Basket(i);
            for (int j = 1; j <= max; j++) {
                if (j % i == 0) {
                    basket.getItemList().add(j);
                }
            }
            basketList.add(basket);
        }
        return basketList;
    }

    private static void calculateItemSupport() {
        for (Basket basket : basketList) {
            for (Integer item : basket.getItemList()) {
                itemSupportList[item]++;
            }
        }
    }

    private static void calculateItemPairOccuredCount() {
        int transactionPairHashKey;
        for (Basket basket : basketList) {
            // basketItemList.sort(ascendingComparator);
            for (int itemIndexI = 0; itemIndexI < basket.getItemList().size(); itemIndexI++) {
                for (int itemIndexJ = itemIndexI + 1; itemIndexJ < basket.getItemList().size(); itemIndexJ++) {
                    // don't waste memory if individual item support is less than threshold ...
                    transactionPairHashKey = (basket.getItemList().get(itemIndexI) * 100) + basket.getItemList().get(itemIndexJ);
                    TransactionPair transactionPair = transactionPairMap.get(transactionPairHashKey);
                    if (transactionPair == null) {
                        transactionPair = new TransactionPair(basket.getItemList().get(itemIndexI), basket.getItemList().get(itemIndexJ));
                        transactionPairMap.put(transactionPairHashKey, transactionPair);
                    }
                    transactionPair.setPairOccuredCount(transactionPair.getPairOccuredCount() + 1);
                }
            }
        }
    }

    private static void showItemSupport() {
        for (int i = 1; i < itemSupportList.length; i++) {
            System.out.print("[" + i + "]:" + itemSupportList[i] + "\t\t");
            if (i % 7 == 0) {
                System.out.println();
            }
        }

        // Show ItemSupport who passed min threshold
        for (int i = 1; i < itemSupportList.length; i++) {
            if (itemSupportList[i] >= THRESHOLD) {
                System.out.print("[" + i + "]:" + itemSupportList[i] + "\t");
            }
        }
    }


    private static void calculateItemSupportForFirstPass() {
        Map<Integer, TransactionPair> map = new TreeMap<Integer, TransactionPair>(transactionPairMap);
        Set set = map.entrySet();
        Iterator iterator = set.iterator();
        AprioriPass aprioriPass = new AprioriPass(passNumber++);
        while (iterator.hasNext()) {
            Map.Entry transactionMap = (Map.Entry) iterator.next();
            TransactionPair transactionPair = (TransactionPair) transactionMap.getValue();
            AprioriTransactionPair aprioriTransactionPair = new AprioriTransactionPair();
            aprioriTransactionPair.setPairOccuredCount(transactionPair.getPairOccuredCount());
            aprioriTransactionPair.addPair(transactionPair.getTransactionPairI(), transactionPair.getTransactionPairJ());
            aprioriPass.addAprioriTransactionPair(aprioriTransactionPair);
        }
        // All Dataset
        System.out.println();
        System.out.println();
        System.out.println("C" + (passNumber - 1) + " - Candidate pairs of size " + (passNumber - 1));
        displayOutputForPass(aprioriPass);

        removeInFrquentItemSet(aprioriPass);
        // All Frequent Dataset
        System.out.println();
        System.out.println("L" + (passNumber - 1) + " - Truly frequent ItemSets of size " + (passNumber - 1) + " => Threshold is " + THRESHOLD);
        displayOutputForPass(aprioriPass);

        aprioriPassList.add(aprioriPass);

    }

    private static Comparator ascendingComparator = new Comparator<Integer>() {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o1 - o2;
        }
    };
}
