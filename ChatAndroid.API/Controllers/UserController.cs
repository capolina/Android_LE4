using System;
using System.Collections.Generic;
using System.IdentityModel.Tokens.Jwt;
using System.Net;
using System.Security.Claims;
using System.Text;
using System.Threading.Tasks;
using ChatAndroid.API.Configuration;
using ChatAndroid.API.Data.InputModels;
using ChatAndroid.API.Data.Models;
using ChatAndroid.API.Data.ViewModels;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Options;
using Microsoft.IdentityModel.Tokens;

namespace ChatAndroid.API.Controllers
{
    [ Route("[controller]") ]
    public class UserController : Controller
    {
        private readonly UserManager<User>   _userManager;
        private readonly SignInManager<User> _signInManager;
        private readonly JwtConfiguration    _jwtOptions;

        public UserController(UserManager<User>          userManager, 
                              SignInManager<User>        signInManager,
                              IOptions<JwtConfiguration> jwtOptions)
        {
            _userManager   = userManager;
            _signInManager = signInManager;
            _jwtOptions    = jwtOptions.Value;
        }
        
        /*[ HttpGet("") ]
        public IActionResult GetConversations()
        {
            return Ok(_userManager.Users);
        }*/
        
        [ HttpPost("login") ]
        public async Task<IActionResult> Login([FromBody] LoginInputModel user)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);

            var res = await _signInManager.PasswordSignInAsync(user.Username, user.Password, false, false);

            if (res.Succeeded)
            {
                var logUser = await _userManager.FindByNameAsync(user.Username);
                var response = new LoginViewModel
                {
                    Token = await GenerateJwtToken(user.Username, logUser)
                };
                return Ok(response);
            }
            
            ModelState.AddModelError("LoginError", "Bad Username or Password");
            return BadRequest(ModelState);
        }
        
        [Authorize]
        [ HttpPost("logout") ]
        public async Task<IActionResult> Logout()
        {
            await _signInManager.SignOutAsync();
            
            return Ok();
        }
        
        [ HttpPost("register") ]
        public async Task<IActionResult> Register([FromBody] RegisterInputModel user)
        {
            IdentityResult result;
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            var newUser = new User
            {
                UserName = user.Username,
                Admin = false,
                Blacklist = false,
                Connecte = false,
                Couleur = user.Couleur ?? "black"
            };
            result = await _userManager.CreateAsync(newUser, user.Password);

            if (result.Succeeded)
            {
                return Ok();
            }

            foreach (var error in result.Errors)
                ModelState.AddModelError(error.Code, error.Description);

            return BadRequest(ModelState);
        }
        
        private async Task<string> GenerateJwtToken(string username, User user)
        {
            var claims = new List<Claim>
            {
                new Claim(JwtRegisteredClaimNames.Sub, username),
                new Claim(JwtRegisteredClaimNames.Jti, Guid.NewGuid().ToString()),
                new Claim(ClaimTypes.NameIdentifier,   user.Id)
            };

            var key     = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_jwtOptions.Key));
            var creds   = new SigningCredentials(key, SecurityAlgorithms.HmacSha256);
            var expires = DateTime.Now.AddDays(Convert.ToDouble(_jwtOptions.Expires));

            var token = new JwtSecurityToken(_jwtOptions.Issuer,
                _jwtOptions.Issuer,
                claims,
                expires : expires,
                signingCredentials : creds);

            return new JwtSecurityTokenHandler().WriteToken(token);
        }
    }
}