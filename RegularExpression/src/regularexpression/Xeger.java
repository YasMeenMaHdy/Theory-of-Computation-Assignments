
package regularexpression;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;
import dk.brics.automaton.State;
import dk.brics.automaton.Transition;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;


public class Xeger {
     public static class FailedRandomWalkException extends Exception {
        public FailedRandomWalkException(String message) {
            super(message);
        }
    }
    private final Automaton automaton;
    private Random random;
     public Xeger(String regex, Random random) {
        assert regex != null;
        assert random != null;
        this.automaton = new RegExp(regex).toAutomaton();
        this.random = random;
    }
    public Xeger(String regex) {
        this(regex, new Random());
    }

  ////////////////////////////
    public String generate() {
        StringBuilder builder = new StringBuilder();
        generate(builder, automaton.getInitialState());
        return builder.toString();
    }
    ////////////////////////////////////////
//        public String generate(int minLength, int maxLength) throws FailedRandomWalkException {
//        final StringBuilder builder = new StringBuilder();
//        int walkLength = 0;
//        State state = automaton.getInitialState();
//
//        // First get to the uniformly distributed target length
//        final int targetLength = Xeger.getRandomInt(minLength, maxLength, random);
//        while (walkLength < targetLength) {
//            List<Transition> transitions = state.getSortedTransitions(false);
//            if (transitions.size() == 0) {
//                if (walkLength >= minLength) {
//                    assert state.isAccept();
//                    return builder.toString();
//                } else {
//                    throw new FailedRandomWalkException(String.format(
//                            "Reached accept state before minimum length (current = %d < min = %d)",
//                            walkLength, minLength));
//                }
//            }
//
//            // Try to prefer non-final transitions if possible at this first stage
//            List<Transition> nonFinalTransitions = transitions.stream()
//                    .filter(t -> !t.getDest().getTransitions().isEmpty()).collect(Collectors.toList());
//            if (!nonFinalTransitions.isEmpty()) {
//                transitions = nonFinalTransitions;
//            }
//
//            final int option = Xeger.getRandomInt(0, transitions.size() - 1, random);
//            final Transition transition = transitions.get(option);
//            appendChoice(builder, transition);
//            state = transition.getDest();
//            ++walkLength;
//        }
//
//        // Now, get to an accept state
//        while (!state.isAccept() && walkLength < maxLength) {
//            List<Transition> transitions = state.getSortedTransitions(false);
//            if (transitions.size() == 0) {
//               assert state.isAccept();
//               return builder.toString();
//            }
//
//            final int option = Xeger.getRandomInt(0, transitions.size() - 1, random);
//            final Transition transition = transitions.get(option);
//            appendChoice(builder, transition);
//            state = transition.getDest();
//            ++walkLength;
//        }
//
//        if (state.isAccept()) {
//            return builder.toString();
//        } else {
//            throw new FailedRandomWalkException(String.format(
//                    "Exceeded maximum walk length (%d) before reaching an accept state: " +
//                            "target length was %d (min length = %d)",
//                    maxLength, targetLength, minLength));
//        }
//    }
        ///////////////////////////////////////////
        private Optional<Transition> appendRandomChoice(StringBuilder builder, State state, int minLength, int walkLength) throws FailedRandomWalkException {
        List<Transition> transitions = state.getSortedTransitions(false);
        if (transitions.size() == 0) {
            if (walkLength >= minLength) {
                return Optional.empty();
            } else {
                throw new FailedRandomWalkException(String.format(
                        "Reached accept state before minimum length (current = %d < min = %d)",
                        walkLength, minLength));
            }
        }

        final int option = Xeger.getRandomInt(0, transitions.size() - 1, random);
        final Transition transition = transitions.get(option);
        appendChoice(builder, transition);
        return Optional.of(transition);
    }
    ///////////////////////////////////////////////
        private void generate(StringBuilder builder, State state) {
        List<Transition> transitions = state.getSortedTransitions(false);
        if (transitions.size() == 0) {
            assert state.isAccept();
            return;
        }
        int nroptions = state.isAccept() ? transitions.size() : transitions.size() - 1;
        int option = Xeger.getRandomInt(0, nroptions, random);
        if (state.isAccept() && option == 0) {          // 0 is considered stop
            return;
        }
        // Moving on to next transition
        Transition transition = transitions.get(option - (state.isAccept() ? 1 : 0));
        appendChoice(builder, transition);
        generate(builder, transition.getDest());
    }
        ////////////////////////////////////////
         private void appendChoice(StringBuilder builder, Transition transition) {
        char c = (char) Xeger.getRandomInt(transition.getMin(), transition.getMax(), random);
        builder.append(c);
    }
///////////////////////////////////
    public Random getRandom() {
        return random;
    }
/////////////////////////////
    public void setRandom(Random random) {
        this.random = random;
    }
    //////////////////////////
    static int getRandomInt(int min, int max, Random random) {
        // Use random.nextInt as it guarantees a uniform distribution
        int maxForRandom = max - min + 1;
        return random.nextInt(maxForRandom) + min;
    }
}
