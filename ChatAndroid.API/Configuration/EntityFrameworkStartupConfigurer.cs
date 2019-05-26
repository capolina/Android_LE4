using ChatAndroid.API.Data;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;

namespace ManageBook.Api.Configuration
{
    public class EntityFrameworkStartupConfigurer
    {
        public static void Configure(IServiceCollection services, IConfiguration configuration)
        {
            services.AddDbContext<ChatAndroidContext>(options =>
                options.UseSqlServer(GetConnectionString(configuration)));
        }

        private static string GetConnectionString(IConfiguration configuration)
        {
            return configuration["ConnectionString"];
        }
    }
}