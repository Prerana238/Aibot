package com.ironlady.service;

import java.util.List;

import com.ironlady.dao.ProgramDAO;
import com.ironlady.model.Program;

public class RecommendationService {

    private ProgramDAO programDAO;

    public RecommendationService() {
        this.programDAO = new ProgramDAO();
    }

    /**
     * Recommends ONE best program based on:
     * target role + interest + experience
     */
    public Program recommendProgram(String target, String interest, String experience) {

        if (target == null || interest == null || experience == null) {
            return null;
        }

        List<Program> programs = programDAO.getAllPrograms();

        Program bestProgram = null;
        int bestScore = -1;

        for (Program p : programs) {

            int score = 0;

            // 1️⃣ Target match (highest priority)
            if (p.getTarget().equalsIgnoreCase(target)) {
                score += 50;
            } else {
                continue; // target MUST match
            }

            String normalizedInterest =
                    interest.replace("_", " ").toLowerCase();

            if (p.getInterest() != null &&
                p.getInterest().toLowerCase().contains(normalizedInterest)) {
                score += 30;
            }


            // 3️⃣ Experience suitability (tie breaker)
            if ("ENTRY".equalsIgnoreCase(experience)) {
                score += 10;
            } else if ("MID".equalsIgnoreCase(experience)) {
                score += 8;
            } else if ("SENIOR".equalsIgnoreCase(experience)) {
                score += 6;
            } else if ("EXEC".equalsIgnoreCase(experience)) {
                score += 5;
            }

            // 4️⃣ Pick highest scoring program
            if (score > bestScore) {
                bestScore = score;
                bestProgram = p;
            }
        }

        return bestProgram;
    }
}
