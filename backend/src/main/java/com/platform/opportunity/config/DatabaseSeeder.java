package com.platform.opportunity.config;

import com.platform.opportunity.model.*;
import com.platform.opportunity.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Configuration
public class DatabaseSeeder {

    @Bean
    CommandLineRunner initDatabase(
            UserRepository userRepository,
            StudentProfileRepository studentProfileRepository,
            CategoryRepository categoryRepository,
            SkillRepository skillRepository,
            InterestRepository interestRepository,
            OpportunityRepository opportunityRepository,
            PasswordEncoder passwordEncoder) {
        
        return args -> {
            // Seed Categories if empty
            if (categoryRepository.count() == 0) {
                categoryRepository.saveAll(List.of(
                        new Category("Internships", "💼"),
                        new Category("Hackathons", "🏆"),
                        new Category("Competitions", "🥇"),
                        new Category("Scholarships", "🎓"),
                        new Category("Courses", "📚"),
                        new Category("Certifications", "📜"),
                        new Category("Workshops", "🛠")
                ));
            }

            // Seed Skills
            if (skillRepository.count() == 0) {
                skillRepository.saveAll(List.of(
                        new Skill("Java"), new Skill("Python"), new Skill("C++"),
                        new Skill("JavaScript"), new Skill("HTML"), new Skill("CSS"),
                        new Skill("SQL"), new Skill("React"), new Skill("Spring Boot"),
                        new Skill("Machine Learning"), new Skill("Data Structures")
                ));
            }

            // Seed Interests
            if (interestRepository.count() == 0) {
                interestRepository.saveAll(List.of(
                        new Interest("Web Development"), new Interest("App Development"),
                        new Interest("AI/ML"), new Interest("Data Science"),
                        new Interest("Cybersecurity"), new Interest("Cloud Computing"),
                        new Interest("IoT"), new Interest("Open Source")
                ));
            }

            // Seed Admin User
            if (!userRepository.existsByEmail("admin@opportunityhub.com")) {
                User admin = new User();
                admin.setEmail("admin@opportunityhub.com");
                admin.setPassword(passwordEncoder.encode("12345678"));
                admin.setRole(Role.ADMIN);
                userRepository.save(admin);
            }

            // Seed Sample Opportunities
            if (opportunityRepository.count() == 0) {
                Category internship = categoryRepository.findAll().stream().filter(c -> c.getName().equals("Internships")).findFirst().orElseThrow();
                Category hackathon = categoryRepository.findAll().stream().filter(c -> c.getName().equals("Hackathons")).findFirst().orElseThrow();
                Category course = categoryRepository.findAll().stream().filter(c -> c.getName().equals("Courses")).findFirst().orElseThrow();

                Skill java = skillRepository.findByName("Java").orElseThrow();
                Skill springBoot = skillRepository.findByName("Spring Boot").orElseThrow();
                Skill sql = skillRepository.findByName("SQL").orElseThrow();
                Skill python = skillRepository.findByName("Python").orElseThrow();
                Skill ml = skillRepository.findByName("Machine Learning").orElseThrow();

                // 1. Java Backend Internship
                Opportunity opp1 = new Opportunity();
                opp1.setTitle("Java Backend Internship");
                opp1.setOrganization("ABC Technologies");
                opp1.setDescription("A 6-month internship focusing on building scalable REST APIs using Java and Spring Boot.");
                opp1.setCategory(internship);
                opp1.getSkillsRequired().addAll(List.of(java, springBoot, sql));
                opp1.setEligibility("3rd or 4th Year B.Tech Students");
                opp1.setLocation("Remote");
                opp1.setMode(Opportunity.Mode.ONLINE);
                opp1.setStartDate(LocalDate.now().plusDays(30));
                opp1.setDeadline(LocalDate.now().plusDays(15));
                opp1.setStipend(new BigDecimal("25000"));
                opp1.setFee(BigDecimal.ZERO);
                opp1.setApplicationUrl("https://example.com/apply/java-intern");
                opp1.setStatus(Opportunity.Status.ACTIVE);
                opportunityRepository.save(opp1);

                // 2. AI/ML Hackathon
                Opportunity opp2 = new Opportunity();
                opp2.setTitle("Global AI Hackathon 2026");
                opp2.setOrganization("Tech Innovators Network");
                opp2.setDescription("Build the future with AI. Compete in a 48-hour online hackathon to solve real-world problems using Machine Learning.");
                opp2.setCategory(hackathon);
                opp2.getSkillsRequired().addAll(List.of(python, ml));
                opp2.setEligibility("All College Students");
                opp2.setLocation("Online");
                opp2.setMode(Opportunity.Mode.ONLINE);
                opp2.setStartDate(LocalDate.now().plusDays(10));
                opp2.setDeadline(LocalDate.now().plusDays(5));
                opp2.setStipend(BigDecimal.ZERO);
                opp2.setFee(BigDecimal.ZERO);
                opp2.setApplicationUrl("https://example.com/hackathon/ai-2026");
                opp2.setStatus(Opportunity.Status.ACTIVE);
                opportunityRepository.save(opp2);
                
                // 3. Web Development Masterclass
                Opportunity opp3 = new Opportunity();
                opp3.setTitle("Modern Web Development Masterclass");
                opp3.setOrganization("Code Academy");
                opp3.setDescription("Learn HTML, CSS, JavaScript and React in this intensive 4-week weekend course.");
                opp3.setCategory(course);
                Skill html = skillRepository.findByName("HTML").orElseThrow();
                Skill css = skillRepository.findByName("CSS").orElseThrow();
                Skill js = skillRepository.findByName("JavaScript").orElseThrow();
                Skill react = skillRepository.findByName("React").orElseThrow();
                opp3.getSkillsRequired().addAll(List.of(html, css, js, react));
                opp3.setEligibility("Beginner Friendly");
                opp3.setLocation("Bangalore (Hybrid)");
                opp3.setMode(Opportunity.Mode.HYBRID);
                opp3.setStartDate(LocalDate.now().plusDays(20));
                opp3.setDeadline(LocalDate.now().plusDays(19));
                opp3.setStipend(BigDecimal.ZERO);
                opp3.setFee(new BigDecimal("999"));
                opp3.setApplicationUrl("https://example.com/courses/web-dev");
                opp3.setStatus(Opportunity.Status.ACTIVE);
                opportunityRepository.save(opp3);
            }
        };
    }
}
