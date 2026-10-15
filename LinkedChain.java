public class LinkedChain<T> {
    private Node<T> head;  // reference to the first node
    private int numOfEntries;

    private static class Node<T> {
        private T data;
        private Node<T> next;

        private Node(T data) {
            this.data = data;
            this.next = null;
        }

        private Node(T data, Node<T> next) {
            this.data = data;
            this.next = next;
        }

        // Added missing Node helper methods
        public T getData() {
            return data;
        }

        public Node<T> getNext() {
            return next;
        }

        public void setNext(Node<T> next) {
            this.next = next;
        }
    }

    public LinkedChain() {
        this.numOfEntries = 0;
        this.head = null;
    }

    public boolean add(T toAdd) {
        if (toAdd == null) {
            return false;
        }
        Node<T> newNode = new Node<>(toAdd);
        if (head == null) {
            head = newNode;
        } else {
            Node<T> currentNode = head;
            while (currentNode.getNext() != null) {
                currentNode = currentNode.getNext();
            }
            currentNode.setNext(newNode);
        }
        numOfEntries++;
        return true;
    }

    public T remove(T toRemove) {
        if (head == null || toRemove == null) {
            return null;
        }
        if (head.getData().equals(toRemove)) {
            T data = head.getData();
            head = head.getNext();
            numOfEntries--;
            return data;
        }
        Node<T> currentNode = head;
        while (currentNode.getNext() != null) {
            if (currentNode.getNext().getData().equals(toRemove)) {
                T data = currentNode.getNext().getData();
                currentNode.setNext(currentNode.getNext().getNext());
                numOfEntries--;
                return data;
            }
            currentNode = currentNode.getNext();
        }
        return null;
    }

    public void clear() {
        head = null;
        numOfEntries = 0;
    }

    public T getIndex(int index) {
        if (index < 0 || index >= numOfEntries) {
            return null;
        }
        Node<T> currentNode = head;
        for (int i = 0; i < index; i++) {
            currentNode = currentNode.getNext();
        }
        return currentNode.getData();
    }

    public boolean contains(T anEntry) {
        if (anEntry == null) return false;
        Node<T> currentNode = head;
        while (currentNode != null) {
            if (currentNode.getData().equals(anEntry)) {
                return true;
            }
            currentNode = currentNode.getNext();
        }
        return false;
    }

    // Fixed: Added int return type
    public int getNumOfEntries() {
        return numOfEntries;
    }

    // Fixed: Added int return type
    public int getInstancesOf(T anEntry) {
        if (anEntry == null) return 0;
        int count = 0;
        Node<T> currentNode = head;
        while (currentNode != null) {
            if (currentNode.getData().equals(anEntry)) {
                count++;
            }
            currentNode = currentNode.getNext();
        }
        return count;
    }

    public LinkedChain<T> filterPrice(String type, double high, double low) {
        LinkedChain<T> filteredList = new LinkedChain<>();
        Node<T> currentNode = head;
        while (currentNode != null) {
            if (currentNode.getData() instanceof Vehicles) {
                Vehicles vehicle = (Vehicles) currentNode.getData();
                // Ensure your Vehicles class has getType() or check appropriate attributes
                if (vehicle.getPrice() <= high && vehicle.getPrice() >= low) {
                    filteredList.add(currentNode.getData());
                }
            }
            currentNode = currentNode.getNext();
        }
        return filteredList;
    }

    public LinkedChain<T> filterAccidents(boolean hasAccident) {
        LinkedChain<T> filteredList = new LinkedChain<>();
        Node<T> currentNode = head;
        while (currentNode != null) {
            if (currentNode.getData() instanceof Vehicles) {
                Vehicles vehicle = (Vehicles) currentNode.getData();
                if (vehicle.hasAccidents() == hasAccident) {
                    filteredList.add(currentNode.getData());
                }
            }
            currentNode = currentNode.getNext();
        }
        return filteredList;
    }
    /**
     * Finds and returns a new LinkedChain containing vehicles matching the specified VIN.
     */
    public LinkedChain<T> findByVin(String vin) {
        LinkedChain<T> resultList = new LinkedChain<>();
        if (vin == null || vin.trim().isEmpty()) {
            return resultList;
        }
        Node<T> currentNode = head;
        while (currentNode != null) {
            if (currentNode.getData() instanceof Vehicles) {
                Vehicles vehicle = (Vehicles) currentNode.getData();
                if (vehicle.getVin() != null && vehicle.getVin().equalsIgnoreCase(vin.trim())) {
                    resultList.add(currentNode.getData());
                }
            }
            currentNode = currentNode.getNext();
        }
        return resultList;
    }

    /**
     * Searches and returns a new LinkedChain of vehicles matching the specified model and color 
     * (handling flexible or optional search parameters).
     */
    public LinkedChain<T> searchByBrandModelColor(String model, String color) {
        LinkedChain<T> resultList = new LinkedChain<>();
        Node<T> currentNode = head;
        while (currentNode != null) {
            if (currentNode.getData() instanceof Vehicles) {
                Vehicles vehicle = (Vehicles) currentNode.getData();
                
                boolean matchesModel = (model == null || model.trim().isEmpty() || 
                                      (vehicle.getModel() != null && vehicle.getModel().equalsIgnoreCase(model.trim())));
                boolean matchesColor = (color == null || color.trim().isEmpty() || 
                                      (vehicle.getColor() != null && vehicle.getColor().equalsIgnoreCase(color.trim())));
                
                if (matchesModel && matchesColor) {
                    resultList.add(currentNode.getData());
                }
            }
            currentNode = currentNode.getNext();
        }
        return resultList;
    }

    /**
     * Removes the vehicle with the specified VIN from the chain.
     * Returns true if successfully removed, false otherwise.
     */
    public boolean removeByVin(String vin) {
        if (vin == null || head == null) {
            return false;
        }
        Node<T> currentNode = head;
        
        // Check if head matches
        if (currentNode.getData() instanceof Vehicles) {
            Vehicles vehicle = (Vehicles) currentNode.getData();
            if (vehicle.getVin() != null && vehicle.getVin().equalsIgnoreCase(vin.trim())) {
                head = head.getNext();
                numOfEntries--;
                return true;
            }
        }
        
        // Check remaining nodes
        while (currentNode.getNext() != null) {
            if (currentNode.getNext().getData() instanceof Vehicles) {
                Vehicles vehicle = (Vehicles) currentNode.getNext().getData();
                if (vehicle.getVin() != null && vehicle.getVin().equalsIgnoreCase(vin.trim())) {
                    currentNode.setNext(currentNode.getNext().getNext());
                    numOfEntries--;
                    return true;
                }
            }
            currentNode = currentNode.getNext();
        }
        return false;
    }
}