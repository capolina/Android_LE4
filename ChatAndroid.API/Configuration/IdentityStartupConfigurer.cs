using System;
using System.IdentityModel.Tokens.Jwt;
using System.Text;
using ChatAndroid.API.Data;
using ChatAndroid.API.Data.Models;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Identity;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.IdentityModel.Tokens;

namespace ChatAndroid.API.Configuration
{
    public class IdentityStartupConfigurer
    {
        public static void Configure(IServiceCollection services, IConfiguration configuration)
        {
            services.Configure<JwtConfiguration>(configuration.GetSection("Jwt"));

            services.AddIdentity<User, IdentityRole>()
                    .AddEntityFrameworkStores<ChatAndroidContext>()
                    .AddDefaultTokenProviders();

            var jwtOptions = new JwtConfiguration();
            configuration.GetSection("Jwt").Bind(jwtOptions);
            var encodedKey = Encoding.UTF8.GetBytes(jwtOptions.Key);

            JwtSecurityTokenHandler.DefaultInboundClaimTypeMap.Clear();
            services.AddAuthentication(options =>
                                       {
                                           options.DefaultAuthenticateScheme = JwtBearerDefaults.AuthenticationScheme;
                                           options.DefaultScheme             = JwtBearerDefaults.AuthenticationScheme;
                                           options.DefaultChallengeScheme    = JwtBearerDefaults.AuthenticationScheme;
                                       })
                    .AddJwtBearer(cfg =>
                                  {
                                      cfg.RequireHttpsMetadata      = false;
                                      cfg.SaveToken                 = true;
                                      cfg.TokenValidationParameters = new TokenValidationParameters
                                                                      {
                                                                          ValidIssuer      = jwtOptions.Issuer,
                                                                          ValidAudience    = jwtOptions.Issuer,
                                                                          IssuerSigningKey =
                                                                              new SymmetricSecurityKey(encodedKey),
                                                                          ClockSkew = TimeSpan.Zero
                                                                      };
                                  });
        }
    }
}
