# Environment Configuration Guide

This file contains instructions for setting up environment variables for the DubleFin Banking application.

## Required Environment Variables

### Database Configuration
```env
DB_USERNAME=root
DB_PASSWORD=your_secure_password_here
```

### JWT Configuration
```env
# Generate a strong secret key (minimum 64 characters recommended)
JWT_SECRET=yourSecretKeyForJWTWhichShouldBeVeryLongAndSecureForProductionUseChangeThisInProduction
```

### Service Discovery
```env
EUREKA_PASSWORD=eureka_secure_password
```

### Config Server
```env
CONFIG_PASSWORD=config_secure_password
```

### Email Configuration (for Notification Service)
```env
EMAIL_USERNAME=your-email@gmail.com
EMAIL_PASSWORD=your-app-specific-password
```

## Setting Up Gmail for Notifications

1. Go to your Google Account settings
2. Enable 2-Step Verification
3. Generate an App Password:
   - Go to Security > 2-Step Verification > App passwords
   - Select "Mail" and your device
   - Copy the generated 16-character password
4. Use this password as EMAIL_PASSWORD

## Environment Files

### For Docker Compose
Create a `.env` file in the `backend-springboot` directory:
```env
DB_USERNAME=root
DB_PASSWORD=root
JWT_SECRET=yourSecretKeyForJWTWhichShouldBeVeryLongAndSecureForProductionUse
EUREKA_PASSWORD=eureka123
CONFIG_PASSWORD=config123
EMAIL_USERNAME=your-email@gmail.com
EMAIL_PASSWORD=your-app-password
```

### For Local Development
Set environment variables in your IDE or terminal:

**Windows (PowerShell):**
```powershell
$env:DB_USERNAME="root"
$env:DB_PASSWORD="root"
$env:JWT_SECRET="yourSecretKeyForJWTWhichShouldBeVeryLongAndSecureForProductionUse"
```

**Linux/Mac:**
```bash
export DB_USERNAME=root
export DB_PASSWORD=root
export JWT_SECRET=yourSecretKeyForJWTWhichShouldBeVeryLongAndSecureForProductionUse
```

## Security Best Practices

1. **Never commit .env files** - Add `.env` to `.gitignore`
2. **Use strong passwords** - Minimum 16 characters with mixed case, numbers, and symbols
3. **Rotate secrets regularly** - Change JWT secrets and passwords periodically
4. **Use different secrets for different environments** - Dev, staging, and production should have different credentials
5. **Store production secrets securely** - Use AWS Secrets Manager, Azure Key Vault, or HashiCorp Vault

## Production Considerations

For production deployment:
- Use environment-specific secret management
- Enable HTTPS/TLS
- Use strong, randomly generated secrets
- Implement secret rotation
- Use managed database services
- Enable audit logging
- Set up monitoring and alerting

