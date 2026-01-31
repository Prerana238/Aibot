package com.ironlady.controller;

import com.ironlady.model.Program;
import com.ironlady.service.RecommendationService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/chat")
public class ChatServlet extends HttpServlet {

    private RecommendationService service = new RecommendationService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();
        String message = request.getParameter("message");
        if ("__RESTART__".equals(message)) {
            request.getSession().invalidate();
            out.print("Hi 👋 What’s your name?");
            return;
        }

        String stage = (String) session.getAttribute("stage");

        // ---------- START ----------
        if (stage == null) {
            session.setAttribute("stage", "ASK_NAME");
            out.print("Hi 👋 What’s your name?");
            return;
        }

        // ---------- NAME ----------
        if ("ASK_NAME".equals(stage)) {

            if (message.length() < 2 || message.equalsIgnoreCase("hi")) {
                out.print("😊 Please tell me your name (not just hi)");
                return;
            }

            session.setAttribute("name", message);
            session.setAttribute("stage", "ASK_EDUCATION");
            out.print("Nice to meet you, " + message + "! 🎓 Please select your education:");
            return;
        }

        // ---------- EDUCATION ----------
        if ("ASK_EDUCATION".equals(stage)) {
            session.setAttribute("education", message);
            session.setAttribute("stage", "ASK_INTEREST");

            out.print("Great 👍 What is your area of interest?");
            return;
        }

        // ---------- INTEREST ----------
        if ("ASK_INTEREST".equals(stage)) {
            session.setAttribute("interest", message);
            session.setAttribute("stage", "ASK_EXPERIENCE");

            out.print("How many years of experience do you have?");
            return;
        }

        // ---------- EXPERIENCE ----------
        if ("ASK_EXPERIENCE".equals(stage)) {
            session.setAttribute("experience", message);
            session.setAttribute("stage", "ASK_TARGET");

            out.print("What is your target role?");
            return;
        }

        // ---------- FINAL ----------
        if ("ASK_TARGET".equals(stage)) {
            session.setAttribute("target", message);

            String target = (String) session.getAttribute("target");
            String interest = (String) session.getAttribute("interest");
            String experience = (String) session.getAttribute("experience");

            Program bestProgram =
                    service.recommendProgram(target, interest, experience);

            if (bestProgram == null) {
                out.print("Sorry 😔 No suitable program found.");
                return;
            }

            session.invalidate(); // reset chat
            out.print(buildAIResponse(bestProgram));
        }
    }

    // ---------- RESPONSE FORMAT ----------
    private String buildAIResponse(Program p) {
        return "🎯 Best Program for You\n\n" +
               "📌 Program: " + p.getName() + "\n" +
               "👤 Target: " + p.getTarget() + "\n" +
               "💡 Interest: " + p.getInterest() + "\n" +
               "⏳ Duration: " + p.getDuration() + " days\n" +
               "🚀 Outcome: " + p.getOutcome();
    }
}
