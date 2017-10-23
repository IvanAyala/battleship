package com.ivan.battleship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class BattleshipApplication {

	public static void main(String[] args) {
		SpringApplication.run(BattleshipApplication.class, args);
	}

	@Autowired
	PlayerRepository playerRepository;

	@Autowired
	GameRepository gameRepository;

	@Autowired
	GamePlayerRepository gamePlayerRepository;

	@Autowired
	ShipRepository shipRepository;

	@Autowired
	SalvoesRepository salvoesRepository;

	@Autowired
	ScoreRepository scoreRepository;



	@Bean
	public CommandLineRunner initData() {
		return (args) -> {
			// save a couple of customers
			Player jack = new Player("j.bauer@ctu.gov", "24");
			playerRepository.save(jack);

			Player coby = new Player("c.obrian@ctu.gov", "42");
			playerRepository.save(coby);

			Player kim = new Player("kim_bauer@gmail.com", "kb");
			playerRepository.save(kim);

			Player timbo = new Player("t.almeida@ctu.gov", "mole");
			playerRepository.save(timbo);

			Player palmer = new Player("d.palmer@whitehouse.gov");
			playerRepository.save(palmer);


			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy, K:m:s a");


			Game game1 = new Game( format.parse("2/17/2018, 3:20:15 PM") );
			gameRepository.save(game1);

			GamePlayer game1Jack = new GamePlayer(game1, jack);
			gamePlayerRepository.save(game1Jack);
			GamePlayer game1Coby = new GamePlayer(game1, coby);
			gamePlayerRepository.save(game1Coby);

			Game game2 = new Game( format.parse("2/17/2017, 4:20:15 PM"));
			gameRepository.save(game2);

			GamePlayer game2Jack = new GamePlayer(game2, jack);
			gamePlayerRepository.save(game2Jack);
			GamePlayer game2Coby = new GamePlayer(game2, coby);
			gamePlayerRepository.save(game2Coby);

			Game game3 = new Game(format.parse("2/17/2017, 5:20:15 PM"));
			gameRepository.save(game3);

			gamePlayerRepository.save( new GamePlayer(game3, coby) );
			gamePlayerRepository.save( new GamePlayer(game3, timbo) );

			Game game4 = new Game( format.parse("2/17/2017, 6:20:15 PM") );
			gameRepository.save(game4);

			gamePlayerRepository.save( new GamePlayer(game4, jack) );
			gamePlayerRepository.save( new GamePlayer(game4, coby) );

			Game game5 = new Game( format.parse("10/5/2017, 7:20:15 PM") );
			gameRepository.save(game5);

			gamePlayerRepository.save( new GamePlayer(game5, timbo) );
			gamePlayerRepository.save( new GamePlayer(game5, jack) );

			Game game6 = new Game( format.parse("10/13/2017, 8:20:15 AM") );
			gameRepository.save(game6);

			gamePlayerRepository.save( new GamePlayer(game6, palmer) );



			/*List<String> locationsDestroyerG1Jack = Arrays.asList("H2","H3","H4");
			Ship destroyerG1Jack = new Ship("destroyer", game1Jack, locationsDestroyerG1Jack);
			shipRepository.save(destroyerG1Jack);  my variable descriptions, too long*/

			List<String> locations = Arrays.asList("H2","H3","H4");
			Ship jacksDestroyer = new Ship("Destroyer", game1Jack, locations);
			shipRepository.save(jacksDestroyer);

			List<String> locationsSubmarineG1Jack = Arrays.asList("E1","F1","G1");
			Ship submarineG1Jack = new Ship("submarine", game1Jack, locationsSubmarineG1Jack);
			shipRepository.save(submarineG1Jack);

			List<String> locationsPatrolBoatG1Jack = Arrays.asList("B4","B5");
			Ship patrolBoatG1Jack = new Ship("patrolBoat", game1Jack, locationsPatrolBoatG1Jack);
			shipRepository.save(patrolBoatG1Jack);

			List<String> locationsPatrolBoatG1Coby = Arrays.asList("F1","F2");
			Ship patrolBoatG1Coby = new Ship("patrolBoat", game1Coby, locationsPatrolBoatG1Coby);
			shipRepository.save(patrolBoatG1Coby);

			List<String> locationsDestroyerG1Coby = Arrays.asList("B5","C5","D5");
			Ship destroyerG1Coby = new Ship("destroyer", game1Coby, locationsDestroyerG1Coby);
			shipRepository.save(destroyerG1Coby);

			List<String> locationsDestroyerG2Jack = Arrays.asList("B5","C5","D5");
			Ship destroyerG2Jack = new Ship("destroyer", game2Jack, locationsDestroyerG2Jack);
			shipRepository.save(destroyerG2Jack);

			List<String> locationsPatrolBoatG2Jack = Arrays.asList("C6","C7");
			Ship PatrolBoatG2Jack = new Ship("patrolBoat", game2Jack, locationsPatrolBoatG2Jack);
			shipRepository.save(PatrolBoatG2Jack);

			List<String> locationsSubmarineG2Coby = Arrays.asList("A2","A3","A4");
			Ship submarineG2Coby = new Ship("submarine", game2Coby, locationsSubmarineG2Coby);
			shipRepository.save(submarineG2Coby);

			List<String> locationsPatrolBoatG2Coby = Arrays.asList("A2","A3","A4");
			Ship patrolBoatG2Coby = new Ship("patrolBoat", game2Coby, locationsPatrolBoatG2Coby);
			shipRepository.save(patrolBoatG2Coby);

			List<String> jackFireLocation1 = Arrays.asList("B5","C5","F1");
			Salvoes game1Round1SalvoesJack = new Salvoes(1, game1Jack, jackFireLocation1);
			salvoesRepository.save(game1Round1SalvoesJack);

			List<String> cobyFireLocation1 = Arrays.asList("B4","B5","B6");
			Salvoes game1Round1SalvoesCoby = new Salvoes(1, game1Coby, cobyFireLocation1);
			salvoesRepository.save(game1Round1SalvoesCoby);


			scoreRepository.save(new Score(game1, jack, Score.SCORE_WINNER ));
			scoreRepository.save(new Score(game1,coby, Score.SCORE_LOSE));

			scoreRepository.save(new Score(game2,jack,Score.SCORE_TIE));
			scoreRepository.save(new Score(game2,coby,Score.SCORE_TIE));

			scoreRepository.save(new Score(game3,coby,Score.SCORE_WINNER));

		};


	}
}

@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

	@Autowired
	PlayerRepository playerRepository;

	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService());
	}

	@Bean
	UserDetailsService userDetailsService() {
		return new UserDetailsService() {

			@Override
			public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
				List<Player> players = playerRepository.findByUserName(userName);
				if (!players.isEmpty()) {
					Player player = players.get(0);
					return new User(player.getUserName(), player.getPassword(),
							AuthorityUtils.createAuthorityList("USER"));
				} else {
					throw new UsernameNotFoundException("Unknown user: " + userName);
				}
			}
		};
	}
}




@EnableWebSecurity
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests()
				.antMatchers("/api/login").permitAll()
				.antMatchers("/api/players").permitAll()
				.antMatchers("/web/**").permitAll()
				.antMatchers("/**").hasAuthority("USER")
				.and()
				.formLogin();

		http.formLogin()
				.usernameParameter("username")
				.passwordParameter("password")
				.loginPage("/api/login");

		http.logout().logoutUrl("/api/logout");


		http.csrf().disable();

		// if user is not authenticated, just send an authentication failure response
		http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if login is successful, just clear the flags asking for authentication
		http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

		// if login fails, just send an authentication failure response
		http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if logout is successful, just send a success response
		http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

	}

	private void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		}
	}
}

