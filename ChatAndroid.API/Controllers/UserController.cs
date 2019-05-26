using System;
using System.Collections.Generic;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;
using System.Threading.Tasks;
using ChatAndroid.API.Configuration;
using ChatAndroid.API.Data.InputModels;
using ChatAndroid.API.Data.Models;
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
        
        [ HttpGet("") ]
        public IActionResult GetConversations()
        {
            return Ok(_userManager.Users);
        }
        
        [ HttpPost("login") ]
        public async Task<IActionResult> Login([FromBody] LoginInputModel user)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);

            var res = await _signInManager.PasswordSignInAsync(user.Username, user.Password, false, false);

            if (res.Succeeded)
            {
                var logUser = await _userManager.FindByNameAsync(user.Username);
                return Ok(await GenerateJwtToken(user.Username, logUser));
            }
            
            ModelState.AddModelError("LoginError", "Bad Username or Password");
            return BadRequest(ModelState);
        }
        
        [ HttpPost("") ]
        public async Task<IActionResult> AddUser([FromBody] User user)
        {
            var nuser = new User();
            nuser.UserName = "Coco";
            await _userManager.CreateAsync(nuser, "Admin1!!");
            return Ok(nuser);
        }
        
        /*[ HttpPost ]
        [ Authorize(Roles = Roles.Admin) ]
        [ Route("") ]
        public async Task<IActionResult> CreateUser([ FromBody ] PetlandUser model)
        {
            try
            {
                IdentityResult result;
                if (!ModelState.IsValid)
                {
                    return BadRequest(ModelState);
                }


                model.PhoneNumber = FormatPhoneNumber(model.PhoneNumber);
                
                if (await _userManager.Users.CountAsync(x => x.PhoneNumber == model.PhoneNumber) != 0)
                {
                    ModelState.AddModelError("duplicatePhoneNumber", $@"Phone number '{model.PhoneNumber}' is already taken.");
                    return BadRequest(ModelState);
                }
                
                var user = new PetlandUser
                           {
                               FirstName   = model.FirstName,
                               LastName    = model.LastName,
                               UserName    = model.UserName,
                               PhoneNumber = model.PhoneNumber,
                               Email       = model.Email,
                               Address1    = model.Address1,
                               Address2    = model.Address2,
                               City        = model.City,
                               State       = model.State,
                               Zip         = model.Zip,
                               Country     = model.Country
                           };

                var password = model.Password != "" ? model.Password : GenerateRandomPassword();

                result = await _userManager.CreateAsync(user, password);

                if (result.Succeeded)
                {
                    result = await _userManager.AddToRoleAsync(user, Roles.Customer);
                    if (result.Succeeded)
                    {
                        if (model.IsAdmin)
                        {
                            result = await _userManager.AddToRoleAsync(user, Roles.Admin);
                        }

                        if (result.Succeeded)
                        {
                            return Ok();
                        }
                    }
                }

                foreach (var error in result.Errors)
                    ModelState.AddModelError(error.Code, error.Description);

                return BadRequest(ModelState);
            }
            catch (Exception ex)
            {
                Logger.Error(ex, "Failure while creating new user.");
                return StatusCode(500);
            }
        }*/
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